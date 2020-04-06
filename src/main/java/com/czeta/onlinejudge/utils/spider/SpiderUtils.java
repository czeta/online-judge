package com.czeta.onlinejudge.utils.spider;

import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudge.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudge.utils.spider.request.SpiderRequest;
import com.czeta.onlinejudge.utils.spider.request.SpiderRequestBody;
import com.czeta.onlinejudge.utils.spider.response.SpiderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.util.Map;


/**
 * @ClassName HttpClientUtils
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:01
 * @Version 1.0
 */
@Slf4j
public class SpiderUtils {
    /**
     *
     * @param request 请求信息
     * @param processor 响应的回调处理函数
     */
    public static Object exec(SpiderRequest request, SpiderPageProcessor processor) {
        // 初始化client
        CloseableHttpClient httpClient = initHttpClient(request);
        CloseableHttpResponse response = null;
        try {
            SpiderRequestBody spiderRequestBody = request.getSpiderRequestBody();
            if (request.getMethod().equals(SpiderConstant.Method.GET)) {
                HttpGet httpGet = new HttpGet(request.getUrl());
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
                response = httpClient.execute(httpGet);
            } else if (request.getMethod().equals(SpiderConstant.Method.POST)) {
                ByteArrayEntity entity =  new ByteArrayEntity(spiderRequestBody.getBody(), spiderRequestBody.getContentType());
                HttpPost httpPost = new HttpPost(request.getUrl());
                httpPost.setEntity(entity);
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
                response = httpClient.execute(httpPost);
            } else {
                // ..
                throw new Exception("暂不支持除GET和POST之外的请求方式");
            }
            SpiderResponse spiderResponse = SpiderResponse.build(request, response);
            // 服务器重定向处理
            if (spiderResponse.getStatusCode() == SpiderConstant.StatusCode.CODE_302) {
                SpiderRequest redirectRequest = new SpiderRequest();
                redirectRequest.setUrl(spiderResponse.getHeaders().get("Location"));
                redirectRequest.setMethod(SpiderConstant.Method.GET);
                redirectRequest.setCookies(request.getCookies());
                return exec(redirectRequest, processor);
            } else {
                // 回调函数处理页面
                return processor.process(spiderResponse);
            }
        } catch (Exception e) {
            log.error("SpiderUtils Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static CloseableHttpClient initHttpClient(SpiderRequest request) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> entry : request.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());
            cookie.setVersion(0);
            String domain = request.getUrl().substring(request.getUrl().indexOf("://") + 3);
            cookie.setDomain(domain.substring(0, domain.indexOf("/")));
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }
        return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }
}

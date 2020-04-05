package com.czeta.onlinejudge.utils.spider.response;


import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudge.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudge.utils.spider.request.SpiderRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SpiderResponse
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:07
 * @Version 1.0
 */
@Slf4j
@Data
public class SpiderResponse {
    private SpiderRequest request;

    private int statusCode = 200;
    private String contentType;
    private String charSet;
    private Map<String, String> headers;
    private Document document;
    private JSONObject jsonObject;
    private String rawText;

    public static SpiderResponse build(SpiderRequest request, HttpResponse response) throws IOException {
        SpiderResponse spiderResponse = new SpiderResponse();
        spiderResponse.setRequest(request);
        spiderResponse.setStatusCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String contentTypeStr = entity.getContentType().getValue();
        spiderResponse.setContentType(contentTypeStr.substring(0, contentTypeStr.indexOf(";")));
        spiderResponse.setCharSet(contentTypeStr.substring(contentTypeStr.lastIndexOf("=") + 1));
        Map<String, String> headers = new HashMap<>();
        for (Header h : response.getAllHeaders()) {
            headers.put(h.getName(), h.getValue());
        }
        spiderResponse.setHeaders(headers);
        StringWriter writer = new StringWriter();
        IOUtils.copy(entity.getContent(), writer, StandardCharsets.UTF_8.name());
        if (spiderResponse.getContentType().equals(SpiderConstant.ContentType.JSON)) {
            spiderResponse.setJsonObject(JSONObject.parseObject(writer.toString()));
        } else if (spiderResponse.getContentType().equals(SpiderConstant.ContentType.HTML)){
            spiderResponse.setDocument(Jsoup.parse(writer.toString()));
        }
        spiderResponse.setRawText(writer.toString());
        log.info("SpiderResponse={}", JSONObject.toJSONString(spiderResponse));
        return spiderResponse;
    }
}

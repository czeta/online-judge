package com.czeta.onlinejudge.utils.spider.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SpiderRequest
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:06
 * @Version 1.0
 */
@Data
public class SpiderRequest {
    private String url;
    private String method;
    private SpiderRequestBody spiderRequestBody;
    private Map<String, String> cookies = new HashMap();
    private Map<String, String> headers = new HashMap();

    public SpiderRequest() {}

    public SpiderRequest(String url) {
        this.url = url;
    }

    public SpiderRequest(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public static SpiderRequest build(String url) {
        return new SpiderRequest(url);
    }

    public SpiderRequest addCookie(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    public SpiderRequest addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }
}

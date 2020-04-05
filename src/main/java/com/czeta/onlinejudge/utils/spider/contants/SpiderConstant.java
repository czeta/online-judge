package com.czeta.onlinejudge.utils.spider.contants;

/**
 * @ClassName HttpConstant
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:22
 * @Version 1.0
 */
public class SpiderConstant {
    public SpiderConstant() {
    }

    public abstract static class Header {
        public static final String REFERER = "Referer";
        public static final String USER_AGENT = "User-Agent";

        public Header() {
        }
    }

    public abstract static class StatusCode {
        public static final int CODE_200 = 200;
        public static final int CODE_302 = 302;

        public StatusCode() {
        }
    }

    public abstract static class Method {
        public static final String GET = "GET";
        public static final String HEAD = "HEAD";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String TRACE = "TRACE";
        public static final String CONNECT = "CONNECT";

        public Method() {
        }
    }

    public abstract static class ContentType {
        public static final String JSON = "application/json";
        public static final String XML = "text/xml";
        public static final String FORM = "application/x-www-form-urlencoded";
        public static final String MULTIPART = "multipart/form-data";
        public static final String HTML = "text/html";

        public ContentType() {
        }
    }
}

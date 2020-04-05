package com.czeta.onlinejudge.utils.spider.request;

import lombok.Data;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SpiderRequestBody
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:14
 * @Version 1.0
 */
@Data
public class SpiderRequestBody {
    private byte[] body;
    private ContentType contentType;
    private String encoding;

    public SpiderRequestBody() {
    }

    public SpiderRequestBody(byte[] body, ContentType contentType, String encoding) {
        this.body = body;
        this.contentType = contentType;
        this.encoding = encoding;
    }

    public static SpiderRequestBody json(String json, String encoding) {
        try {
            return new SpiderRequestBody(json.getBytes(encoding), ContentType.APPLICATION_JSON, encoding);
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException("illegal encoding " + encoding, var3);
        }
    }

    public static SpiderRequestBody xml(String xml, String encoding) {
        try {
            return new SpiderRequestBody(xml.getBytes(encoding), ContentType.APPLICATION_XML, encoding);
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException("illegal encoding " + encoding, var3);
        }
    }

    public static SpiderRequestBody form(Map<String, Object> params, String encoding) {
        List<NameValuePair> nameValuePairs = new ArrayList(params.size());
        Iterator var3 = params.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var3.next();
            nameValuePairs.add(new BasicNameValuePair((String)entry.getKey(), String.valueOf(entry.getValue())));
        }

        try {
            return new SpiderRequestBody(URLEncodedUtils.format(nameValuePairs, encoding).getBytes(encoding), ContentType.APPLICATION_FORM_URLENCODED, encoding);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalArgumentException("illegal encoding " + encoding, var5);
        }
    }

    public static SpiderRequestBody custom(byte[] body, ContentType contentType, String encoding) {
        return new SpiderRequestBody(body, contentType, encoding);
    }

}

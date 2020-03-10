package com.czeta.onlinejudge.utils.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @ClassName HttpUtils
 * @Description HTTP工具类
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public final class HttpUtils {

    private static String UTF8 = "UTF-8";
    private static String CONTENT_TYPE = "application/json";

    private HttpUtils(){
        throw new AssertionError();
    }

    /**
     * 返回给前端信息
     * @param response
     * @param object
     * @throws Exception
     */
    public static void printJSON(HttpServletResponse response, Object object) throws Exception{
        response.setCharacterEncoding(UTF8);
        response.setContentType(CONTENT_TYPE);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(object));
        printWriter.flush();
        printWriter.close();
    }
}
package com.czeta.onlinejudge.utils.utils;

import java.util.UUID;

/**
 * uuid工具类
 */
/**
 * @ClassName UUIDUtils
 * @Description uuid工具类
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class UUIDUtils {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

}

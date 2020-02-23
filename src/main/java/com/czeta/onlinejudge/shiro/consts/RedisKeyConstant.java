package com.czeta.onlinejudge.shiro.consts;

/**
 * @ClassName RedisKeyConstant
 * @Description shiro redis key常量
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class RedisKeyConstant {

    /**
     * 登陆用户信息key，%s是username。记录用户登录信息。
     */
    public static final String LOGIN_USER = "login:user:%s";

    /**
     * 登陆用户token信息key，%s是md5token。记录用户token信息。
     */
    public static final String LOGIN_TOKEN = "login:token:%s";

    /**
     * LOGIN_TOKEN的key，两个%s分别是username md5token。该key是用来记录某个用户的所有 LOGIN_TOKEN的key。
     */
    public static final String LOGIN_USER_TOKEN = "login:user:token:%s:%s";
}

package com.czeta.onlinejudge.shiro.cache;


import com.czeta.onlinejudge.shiro.jwt.JwtToken;
import com.czeta.onlinejudge.shiro.model.result.LoginUserModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserRedisModel;

/**
 * @InterfaceName LoginRedisService
 * @Description 登陆信息Redis缓存操作服务
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface LoginRedisService {
    /**
     * 缓存登陆信息
     * @param jwtToken
     * @param loginUserModel
     */
    void cacheLoginInfo(JwtToken jwtToken, LoginUserModel loginUserModel);

    /**
     * 刷新登陆信息
     * @param oldToken
     * @param username
     * @param newJwtToken
     */
    void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken);

    /**
     * 通过用户名，从缓存中获取登陆用户LoginUserRedisModel
     * @param username
     * @return
     */
    LoginUserRedisModel getLoginUserRedisModel(String username);

    /**
     * 删除对应用户登录有关的Redis缓存
     * @param token
     * @param username
     */
    void deleteUserLoginInfo(String token, String username);

    /**
     * 删除对应用户所有登陆缓存
     * @param username
     */
    void deleteUserAllCache(String username);

    /**
     * 通过用户名称获取盐值
     * @param username
     * @return
     */
    String getSalt(String username);

    /**
     * 判断token在redis中是否存在
     * @param token
     * @return
     */
    boolean exists(String token);
}

package com.czeta.onlinejudge.shiro.service;

import com.czeta.onlinejudge.shiro.jwt.JwtToken;
import com.czeta.onlinejudge.shiro.model.param.LoginParamModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @InterfaceName LoginService
 * @Description 登录服务接口
 * @Author chenlongjie
 * @Date 2020/2/23 12:56
 * @Version 1.0
 */
public interface LoginService {

    /**
     * 登陆
     * @param loginParamModel
     * @return
     */
    String login(LoginParamModel loginParamModel);

    /**
     * 退出
     * @param request
     */
    void logout(HttpServletRequest request);

    /**
     * （1）如果(当前时间+倒计时) > 过期时间，表示token过期，刷新token，并更新缓存
     * （2）如果token过期，且redis中无该token，那么表示当前token失效，直接返回提示用户重新登录
     * （3）如果token过期，redis中有该token，那么只要刷新token并更新缓存
     * @param jwtToken
     * @param httpServletResponse
     */
    void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse);
}

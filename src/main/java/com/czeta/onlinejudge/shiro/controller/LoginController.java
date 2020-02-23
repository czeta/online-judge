package com.czeta.onlinejudge.shiro.controller;

import com.czeta.onlinejudge.shiro.model.param.LoginParamModel;
import com.czeta.onlinejudge.shiro.service.LoginService;
import com.czeta.onlinejudge.shiro.util.JwtTokenWebUtil;
import com.czeta.onlinejudge.util.response.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName LoginController
 * @Description 登陆控制器
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping(value = "/login")
    public APIResult login(@RequestBody LoginParamModel loginParamModel, HttpServletResponse response) {
        String token = loginService.login(loginParamModel);
        response.setHeader(JwtTokenWebUtil.getTokenName(), token);
        return new APIResult();
    }


    @PostMapping("/logout")
    public APIResult logout(HttpServletRequest request) {
        loginService.logout(request);
        return new APIResult();
    }
}

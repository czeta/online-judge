package com.czeta.onlinejudge.shiro.controller;

import com.czeta.onlinejudge.shiro.model.param.LoginParamModel;
import com.czeta.onlinejudge.shiro.service.LoginService;
import com.czeta.onlinejudge.shiro.util.JwtTokenWebUtil;
import com.czeta.onlinejudge.util.response.APIResult;
import io.swagger.annotations.*;
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
@Api(tags = "Login Features Controller")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "登录", notes = "不用token字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginParamModel", value = "登录参数：用户名与密码", dataType = "LoginParamModel", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 3002, message = "用户名或密码错误")
    })
    @PostMapping(value = "/login")
    public APIResult login(@RequestBody LoginParamModel loginParamModel, HttpServletResponse response) {
        String token = loginService.login(loginParamModel);
        response.setHeader(JwtTokenWebUtil.getTokenName(), token);
        return new APIResult();
    }

    @ApiOperation(value = "退出", notes = "不用token字段")
    @ApiImplicitParams({})
    @ApiResponses({})
    @PostMapping("/logout")
    public APIResult logout(HttpServletRequest request) {
        loginService.logout(request);
        return new APIResult();
    }
}

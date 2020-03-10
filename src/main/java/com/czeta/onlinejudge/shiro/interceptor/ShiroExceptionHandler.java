package com.czeta.onlinejudge.shiro.interceptor;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.response.APIResult;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ShiroExceptionHandler
 * @Description shiro异常处理拦截器
 * @Author chenlongjie
 * @Date 2020/3/3 11:25
 * @Version 1.0
 */
@ControllerAdvice
public class ShiroExceptionHandler {
    /**
     * 封装越权访问通用返回结果
     * @param e
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public APIResult apiRuntimeExceptionHandler(UnauthorizedException e) {
        APIResult apiResult = new APIResult(IBaseStatusMsg.APIEnum.AUTHORITY_EXCEED.getCode(), e.getMessage());
        return apiResult;
    }
}

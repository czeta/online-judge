package com.czeta.onlinejudge.util.interceptor;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.exception.APIRuntimeException;
import com.czeta.onlinejudge.util.response.APIResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName CommonExceptionHandler
 * @Description 通用全局异常处理类，包装成通用APIResult接口返回
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = APIRuntimeException.class)
    @ResponseBody
    public APIResult apiRuntimeExceptionHandler(APIRuntimeException e) {
        APIResult apiResult = new APIResult(e.getCode(), e.getMessage());
        return apiResult;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public APIResult exceptionHandler(Exception e) {
        APIResult apiResult = new APIResult(IBaseStatusMsg.APIEnum.FAILED.getCode(), e.getMessage());
        log.error("CommonExceptionHandler exceptionHandler Exception={}", e.getMessage());
        e.printStackTrace();
        return apiResult;
    }
}

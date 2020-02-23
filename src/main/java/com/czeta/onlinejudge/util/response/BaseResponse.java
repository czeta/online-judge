package com.czeta.onlinejudge.util.response;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.exception.IBaseException;

/**
 * @ClassName BaseResponse
 * @Description 基础response类，一般用于rpc
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class BaseResponse implements IBaseResponse {
    private Integer status;
    private String message;

    public BaseResponse() {
        this.status = IBaseStatusMsg.APIEnum.SUCCESS.getCode();
        this.message = IBaseStatusMsg.APIEnum.SUCCESS.getMessage();
    }

    public BaseResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(IBaseStatusMsg statusMsg) {
        this.status = statusMsg.getCode();
        this.message = statusMsg.getMessage();
    }

    public BaseResponse(IBaseStatusMsg statusMsg, String message) {
        this.status = statusMsg.getCode();
        this.message = message;
    }

    public BaseResponse(Exception e) {
        if (e instanceof IBaseException) {
            IBaseException apiException = (IBaseException) e;
            this.status = apiException.getCode();
            this.message = apiException.getMessage();
        } else {
            this.status = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
            this.message = e.getMessage();
        }
    }

    public boolean isSuccess() {
        return IBaseStatusMsg.APIEnum.SUCCESS.getCode().equals(this.getStatus());
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

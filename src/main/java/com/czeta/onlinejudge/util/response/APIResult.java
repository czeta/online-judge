package com.czeta.onlinejudge.util.response;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.util.exception.IBaseException;

public class APIResult<T> {
    private Integer status;
    private String message;
    private T data;

    public APIResult(T data) {
        this();
        this.data = data;
    }

    public APIResult() {
        this(IBaseStatusMsg.APIEnum.SUCCESS);
    }

    public APIResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResult(IBaseStatusMsg statusMsg) {
        this.status = statusMsg.getCode();
        this.message = statusMsg.getMessage();
    }

    public APIResult(Exception e) {
        if (e instanceof IBaseException) {
            IBaseException apiException = (IBaseException) e;
            this.status = apiException.getCode();
            this.message = apiException.getMessage();
        } else {
            this.status = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
            this.message = e.getMessage();
        }
    }

    public APIResult(Exception e, T data) {
        this(e);
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

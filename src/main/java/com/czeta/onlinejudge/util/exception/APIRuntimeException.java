package com.czeta.onlinejudge.util.exception;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;

public class APIRuntimeException extends RuntimeException implements IBaseException {
    private Integer code;
    private String message;

    public APIRuntimeException() {}

    public APIRuntimeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIRuntimeException(IBaseStatusMsg statusMsg) {
        this.code = statusMsg.getCode();
        this.message = statusMsg.getMessage();
    }

    public APIRuntimeException(IBaseStatusMsg statusMsg, String message) {
        this.code = statusMsg.getCode();
        this.message = message == null ? statusMsg.getMessage() : message;
    }

    public APIRuntimeException(Throwable cause) {
        super(cause);
        this.code = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
        this.message = cause.getMessage() == null ? IBaseStatusMsg.APIEnum.SERVER_ERROR.getMessage() : cause.getMessage();
    }

    public APIRuntimeException(Throwable cause, String message) {
        super(message, cause);
        this.code = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

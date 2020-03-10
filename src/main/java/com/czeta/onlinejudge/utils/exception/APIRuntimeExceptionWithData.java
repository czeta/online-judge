package com.czeta.onlinejudge.utils.exception;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;

/**
 * @ClassName APIRuntimeExceptionWithData
 * @Description API运行时异常带信息的类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class APIRuntimeExceptionWithData extends APIRuntimeException {
    private Object data;

    public APIRuntimeExceptionWithData() {
        super();
    }

    public APIRuntimeExceptionWithData(Integer code, String message, Object data) {
        super(code, message);
        this.data = data;
    }

    public APIRuntimeExceptionWithData(IBaseStatusMsg statusMsg, Object data) {
        super(statusMsg);
        this.data = data;
    }

    public APIRuntimeExceptionWithData(IBaseStatusMsg statusMsg, String message, Object data) {
        super(statusMsg, message);
        this.data = data;
    }

    public APIRuntimeExceptionWithData(Throwable cause, Object data) {
        super(cause);
        this.data = data;
    }

    public APIRuntimeExceptionWithData(Throwable cause, String message, Object data) {
        super(cause, message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;

/**
 * @ClassName BaseStatusMsg
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 15:50
 * @Version 1.0
 */
public enum BaseStatusMsg implements IBaseStatusMsg {
    EXISTED_USERNAME(2100, "用户名已存在");

    private Integer code;
    private String message;

    private BaseStatusMsg(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @ClassName JudgeStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 20:24
 * @Version 1.0
 */
public enum JudgeServerStatus implements IEnumItem {
    NORMAL((short) 1, "评测服务正常"),
    STOPPED((short) 0, "评测服务被停用"),
    ABNORMAL((short) -1, "评测服务异常");

    private Short code;
    private String message;

    private JudgeServerStatus(Short code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
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

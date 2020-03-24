package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @ClassName ContestStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/24 16:58
 * @Version 1.0
 */
public enum ContestStatus implements IEnumItem {
    NORMAL((short) 1, "表示正常"),
    END((short) 0, "表示已经结束封榜不能进行任何更改"),
    ABNORMAL((short) -1, "表示下线");

    private Short code;
    private String message;

    private ContestStatus(Short code, String message) {
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

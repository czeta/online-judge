package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @EnumName CertificationStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 14:09
 * @Version 1.0
 */
public enum CommonItemStatus implements IEnumItem {
    ENABLE((short) 1, "启用"),
    DISABLE((short) 0, "尚未启用");

    private Short code;
    private String message;

    private CommonItemStatus(Short code, String message) {
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

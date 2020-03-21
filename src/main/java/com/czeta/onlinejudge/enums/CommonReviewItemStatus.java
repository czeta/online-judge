package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @ClassName CommonReviewItemStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 21:29
 * @Version 1.0
 */
public enum CommonReviewItemStatus implements IEnumItem {
    PASS((short) 1, "通过"),
    TO_REVIEW((short) 0, "待审核"),
    NO_PASS((short) -1, "未通过");

    private Short code;
    private String message;

    private CommonReviewItemStatus(Short code, String message) {
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

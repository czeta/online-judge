package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.util.enums.IEnumItem;

/**
 * @EnumName JudgeType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 19:50
 * @Version 1.0
 */
public enum JudgeTypeEnum implements IEnumItem {
    JUDGE_SPIDER((short) 0, "爬虫评测"),
    JUDGE_MACHINE((short) 1, "评测机评测");

    private Short code;
    private String message;

    private JudgeTypeEnum(Short code, String message) {
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

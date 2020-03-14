package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @EnumName ProblemStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/14 10:40
 * @Version 1.0
 */
public enum ProblemStatus implements IEnumItem {
    NORMAL_VISIBLE((short) 1, "正常并且可以在题目模块可视"),
    NORMAL_INVISIBLE((short) 0, "正常但是不能在题目模块可视，主要是属于比赛的题目"),
    ABNORMAL((short)-1, "题目异常，已下线");

    private Short code;
    private String message;

    private ProblemStatus(Short code, String message) {
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

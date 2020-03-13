package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @EnumName ProblemType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/13 20:22
 * @Version 1.0
 */
public enum ProblemType implements IEnumItem {
    ICPC(0, "ACM/ICPC题型"),
    FUNCTION(1, "函数型题型"),
    SPJ(2, "特判题型");

    private Integer code;
    private String message;


    private ProblemType(Integer code, String message) {
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

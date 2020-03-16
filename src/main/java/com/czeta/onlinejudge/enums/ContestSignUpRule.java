package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

import java.util.HashSet;
import java.util.Set;

/**
 * @EnumName ContestSignUpRule
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 10:30
 * @Version 1.0
 */
public enum ContestSignUpRule implements IEnumItem {
    PUBLIC(0, "公开"),
    CERTIFICATION(1, "认证"),
    PASSWORD(2, "密码");

    private Integer code;
    private String message;


    private ContestSignUpRule(Integer code, String message) {
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

    public static boolean isContain(String msg) {
        Set<String> set = new HashSet<>();
        for (ContestSignUpRule p : ContestSignUpRule.values()) {
            set.add(p.getMessage());
        }
        return set.contains(msg);
    }
}

package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @EnumName ProblemLevel
 * @Description 题目难度枚举
 * @Author chenlongjie
 * @Date 2020/3/14 10:33
 * @Version 1.0
 */
public enum ProblemLevel implements IEnumItem {
    EASY(0, "easy"),
    MEDIUM(1, "medium"),
    HARD(2, "hard");

    private Integer code;
    private String message;


    private ProblemLevel(Integer code, String message) {
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

    public static boolean isContainMessage(String message) {
        for (ProblemLevel p : ProblemLevel.values()) {
            if (p.getMessage().equals(message)) return true;
        }
        return false;
    }

    public static List<String> getEnumMessageList() {
        List<String> list = new ArrayList<>();
        for (ProblemLevel p : ProblemLevel.values()) {
            list.add(p.getMessage());
        }
        return list;
    }
}

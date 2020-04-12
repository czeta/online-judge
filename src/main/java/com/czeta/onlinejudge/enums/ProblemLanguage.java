package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @EnumName ProblemLanguage
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/14 10:35
 * @Version 1.0
 */
public enum ProblemLanguage implements IEnumItem {
    LAN_C(0, "C"),
    LAN_C_PLUS(1, "C++"),
    LAN_JAVA(2, "Java"),
    LAN_PYTHON2(3, "Python2"),
    LAN_PYTHON3(4, "Python3");

    private Integer code;
    private String message;


    private ProblemLanguage(Integer code, String message) {
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

    public static boolean isContainMessage(List<String> list) {
        Set<String> set = new HashSet<>();
        for (ProblemLanguage p : ProblemLanguage.values()) {
            set.add(p.getMessage());
        }
        for (String str : list) {
            if (!set.contains(str)) return false;
        }
        return true;
    }

    public static String getAllProblemLanguage() {
        StringBuffer sb = new StringBuffer();
        for (ProblemLanguage problemLanguage : ProblemLanguage.values()) {
            sb.append(problemLanguage.getMessage());
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
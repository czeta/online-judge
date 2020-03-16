package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @EnumName ContestRankModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 10:30
 * @Version 1.0
 */
public enum ContestRankModel  implements IEnumItem {
    PRACTICE(0, "练习"),
    RATING(1, "积分"),
    ICPC(2, "ACM/ICPC");

    private Integer code;
    private String message;


    private ContestRankModel(Integer code, String message) {
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
        for (ContestRankModel p : ContestRankModel.values()) {
            set.add(p.getMessage());
        }
        return set.contains(msg);
    }
}

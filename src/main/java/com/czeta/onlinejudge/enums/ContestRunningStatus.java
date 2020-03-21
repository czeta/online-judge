package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IEnumItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName ContestRuningStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 16:11
 * @Version 1.0
 */
public enum ContestRunningStatus implements IEnumItem {
    BEFORE_RUNNING(0, "未开始"),
    RUNNING(1, "进行中"),
    AFTER_RUNNING(2, "已结束");

    private Integer code;
    private String message;


    private ContestRunningStatus(Integer code, String message) {
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

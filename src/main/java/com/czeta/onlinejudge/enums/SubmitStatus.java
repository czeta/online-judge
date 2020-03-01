package com.czeta.onlinejudge.enums;

/**
 * @EnumName SubmitStatus
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 16:54
 * @Version 1.0
 */
public enum SubmitStatus {
    ;

    private String name;
    private String message;

    private SubmitStatus() {}
    private SubmitStatus(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

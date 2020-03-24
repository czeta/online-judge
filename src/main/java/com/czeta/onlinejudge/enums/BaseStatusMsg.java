package com.czeta.onlinejudge.enums;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;

/**
 * @ClassName BaseStatusMsg
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 15:50
 * @Version 1.0
 */
public enum BaseStatusMsg implements IBaseStatusMsg {
    EXISTED_USERNAME(2100, "用户名已存在"),
    EXISTED_NAME(2101, "名称已存在"),
    EXISTED_PROBLEM(2102, "题目已存在"),
    EXISTED_PROBLEM_JUDGE_TYPE(2103, "该题已设置评测方式"),
    EXISTED_CONTEST(2104, "比赛已存在"),
    EXISTED_SIGN_UP_CONTEST(2105, "已经申请过比赛"),

    ABNORMAL_PROBLEM(2200, "题目异常，已下线"),
    NORMAL_INVISIBLE_PROBLEM(2201, "无权查看该题：属于比赛题"),
    NORMAL_VISIBLE_PROBLEM(2202, "题目状态有误"),

    INVALID_SIGN_UP(2300, "报名已截止"),
    ENDED_CONTEST(2301, "比赛已经封榜结束(已经计算过积分)"),
    NOT_RATING_CONTEST(2302, "该比赛不是积分赛");

    private Integer code;
    private String message;

    private BaseStatusMsg(Integer code, String message) {
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

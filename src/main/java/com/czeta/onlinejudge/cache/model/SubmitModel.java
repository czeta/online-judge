package com.czeta.onlinejudge.cache.model;

import lombok.Data;

/**
 * @ClassName CacheSubmitModel
 * @Description 缓存的比赛题目提交记录
 * @Author chenlongjie
 * @Date 2020/3/21 11:38
 * @Version 1.0
 */
@Data
public class SubmitModel {
    private Long problemId;
    private Boolean firstBlood;
    private Boolean accept;
    private String acceptTime;
    private Integer errorCount;

    public SubmitModel() {}

    public SubmitModel(Long problemId, Boolean firstBlood, Boolean accept, String acceptTime, Integer errorCount) {
        this.problemId = problemId;
        this.firstBlood = firstBlood;
        this.accept = accept;
        this.acceptTime = acceptTime;
        this.errorCount = errorCount;
    }
}

package com.czeta.onlinejudge.cache.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CacheSubmitModel
 * @Description 缓存的比赛题目提交记录
 * @Author chenlongjie
 * @Date 2020/3/21 11:38
 * @Version 1.0
 */
@ApiModel(description = "比赛题目提交信息")
@Data
public class SubmitItemModel {
    @ApiModelProperty(value = "问题ID")
    private Long problemId;
    @ApiModelProperty(value = "是否为一血（该题第一个通过的人")
    private Boolean firstBlood;
    @ApiModelProperty(value = "是否通过")
    private Boolean accept;
    @ApiModelProperty(value = "通过所花费的时间（从比赛开始到AC的时间")
    private String acceptTime;
    @ApiModelProperty(value = "错误次数")
    private Integer errorCount;

    public SubmitItemModel() {}

    public SubmitItemModel(Long problemId, Boolean firstBlood, Boolean accept, String acceptTime, Integer errorCount) {
        this.problemId = problemId;
        this.firstBlood = firstBlood;
        this.accept = accept;
        this.acceptTime = acceptTime;
        this.errorCount = errorCount;
    }
}

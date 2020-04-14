package com.czeta.onlinejudge.cache.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName RankItem
 * @Description 缓存的比赛排行榜数据
 * @Author chenlongjie
 * @Date 2020/3/21 12:14
 * @Version 1.0
 */
@ApiModel(description = "缓存的比赛排行榜数据model")
@Data
public class RankItemModel {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "提交次数")
    private Integer submitCount;
    @ApiModelProperty(value = "AC数目")
    private Integer acNum;
    @ApiModelProperty(value = "总时间，单位为s")
    private Long totalTime;
    // key为问题id，value问题提交数据。排序规则：按key值大小排序，升序。
    @ApiModelProperty(value = "每道题的提交信息，key为问题ID，value为提交信息")
    private Map<Long, SubmitItemModel> submitMap;
}
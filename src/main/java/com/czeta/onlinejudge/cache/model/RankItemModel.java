package com.czeta.onlinejudge.cache.model;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName RankItem
 * @Description 缓存的比赛排行榜数据
 * @Author chenlongjie
 * @Date 2020/3/21 12:14
 * @Version 1.0
 */
@Data
public class RankItemModel {
    private Long userId;
    private String username;
    private Integer submitCount;
    private Integer acNum;
    private Long totalTime;
    // key为问题id，value问题提交数据。排序规则：按key值大小排序，升序。
    private Map<Long, SubmitModel> submitMap;
}
package com.czeta.onlinejudge.cache.model;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName CacheContestRankModel
 * @Description 缓存比赛排行榜model
 * @Author chenlongjie
 * @Date 2020/3/21 12:14
 * @Version 1.0
 */
@Data
public class CacheContestRankModel {
    private Long contestId;
    // key为用户ID，value为用户排名信息。排序规则：按值规则排序（acNum降序、totalTime升序）。
    private Map<Long, RankItemModel> rankItemMap;
}

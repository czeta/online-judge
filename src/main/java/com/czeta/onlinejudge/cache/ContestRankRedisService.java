package com.czeta.onlinejudge.cache;

import com.czeta.onlinejudge.cache.model.RankItemModel;

import java.util.Map;

/**
 * @InterfaceName ContestRankRedisService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/21 12:30
 * @Version 1.0
 */
public interface ContestRankRedisService {

    /**
     * 通过比赛id，从缓存中获取排名map
     * @param contestId
     * @return
     */
    Map<Long, RankItemModel> getRankItemMapByContestIdFromCache(Long contestId);

    /**
     * 判断缓存是否存在比赛榜单数据
     * @param contestId
     * @return
     */
    boolean exists(Long contestId);

}

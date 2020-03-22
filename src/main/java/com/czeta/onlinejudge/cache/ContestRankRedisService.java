package com.czeta.onlinejudge.cache;

import com.czeta.onlinejudge.cache.model.RankItemModel;

import java.util.List;
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
     * 初始化比赛的缓存榜单数据
     * @param contestId
     * @param problemId
     * @param userId
     */
    void initContestRankRedis(Long contestId, Long problemId, Long userId);

    /**
     * 刷新实时计算比赛榜单数据，这部分需要放在获取评测结果后，更新其它数据前，执行
     * @param contestId
     * @param problemId
     * @param userId
     */
    void refreshContestRankRedis(Long contestId, Long problemId, Long userId, Boolean ac);

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

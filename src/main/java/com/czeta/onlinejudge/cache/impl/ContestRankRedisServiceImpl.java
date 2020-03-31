package com.czeta.onlinejudge.cache.impl;

import com.czeta.onlinejudge.cache.ContestRankRedisService;
import com.czeta.onlinejudge.cache.model.RankItemModel;
import com.czeta.onlinejudge.cache.model.CacheContestRankModel;
import com.czeta.onlinejudge.consts.ContestRankRedisKeyConstant;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName ContestRankRedisServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/21 12:30
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class ContestRankRedisServiceImpl implements ContestRankRedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<Long, RankItemModel> getRankItemMapByContestIdFromCache(Long contestId) {
        if (!exists(contestId)) {
            return null;
        }
        String str = String.valueOf(contestId);
        CacheContestRankModel object = (CacheContestRankModel) redisTemplate.opsForValue().get(String.format(ContestRankRedisKeyConstant.CONTEST_RANK, str));
        return object.getRankItemMap();
    }

    @Override
    public boolean exists(Long contestId) {
        AssertUtils.notNull(contestId, BaseStatusMsg.APIEnum.PARAM_ERROR, "缓存key的contestId为空，不合法");
        String str = String.valueOf(contestId);
        Object object = redisTemplate.opsForValue().get(String.format(ContestRankRedisKeyConstant.CONTEST_RANK, str));
        return object != null;
    }

}
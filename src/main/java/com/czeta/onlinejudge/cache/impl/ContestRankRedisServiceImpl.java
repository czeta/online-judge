package com.czeta.onlinejudge.cache.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.cache.ContestRankRedisService;
import com.czeta.onlinejudge.cache.model.RankItemModel;
import com.czeta.onlinejudge.cache.model.CacheContestRankModel;
import com.czeta.onlinejudge.cache.model.SubmitModel;
import com.czeta.onlinejudge.consts.ContestRankRedisKeyConstant;
import com.czeta.onlinejudge.consts.ContestRankScoreRule;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.SolvedProblem;
import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.SolvedProblemMapper;
import com.czeta.onlinejudge.dao.mapper.SubmitMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.service.ContestService;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Autowired
    private SubmitMapper submitMapper;


    @Override
    public void initContestRankRedis(Long contestId, Long problemId, Long userId) {
        CacheContestRankModel cacheContestRankModel = new CacheContestRankModel();
        cacheContestRankModel.setContestId(contestId);
        cacheContestRankModel.setRankItemMap(new LinkedHashMap<>());

        initContestRankModel(cacheContestRankModel, userId);

        // 缓存有效期设置为比赛结束后一分钟
        Contest contestInfo = contestService.getContestInfo(contestId);
        Duration expireDuration = Duration.ofSeconds(DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime()) - new Date().getTime() / 1000 + 60);
        String keyVal = String.valueOf(contestId);
        redisTemplate.opsForValue().set(String.format(ContestRankRedisKeyConstant.CONTEST_RANK, keyVal), cacheContestRankModel, expireDuration);
    }

    @Override
    public void refreshContestRankRedis(Long contestId, Long problemId, Long userId, Boolean ac) {
        if (!exists(contestId)) {
            return;
        }
        String keyVal = String.valueOf(contestId);
        CacheContestRankModel cacheContestRankModel = (CacheContestRankModel) redisTemplate.opsForValue().get(String.format(ContestRankRedisKeyConstant.CONTEST_RANK, keyVal));
        log.info("rankInfo={}", JSONObject.toJSONString(cacheContestRankModel));
        Map<Long, RankItemModel> rankItemMap = cacheContestRankModel.getRankItemMap();
        RankItemModel rankItemModel = rankItemMap.get(userId);
        rankItemModel = rankItemMap.get(userId);
        rankItemModel.setSubmitCount(rankItemModel.getSubmitCount() + 1);
        // 需要查询该问题是否是用户已经解决过了，而重复提交
        SolvedProblem solvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getProblemId, problemId)
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName()));
        if (ac) {
            rankItemModel.setAcNum(solvedProblem == null ? rankItemModel.getAcNum() + 1 : rankItemModel.getAcNum());
        }
        Long totalTime = rankItemModel.getTotalTime();
        Map<Long, SubmitModel> submitMap = rankItemModel.getSubmitMap();
        Contest contestInfo = contestService.getContestInfo(contestId);
        SubmitModel submitModel = submitMap.get(problemId);
        if (ac) { // 正确答案
            if (solvedProblem == null) { // 尚未解决
                submitModel.setAccept(true);
                // 计算当前时间和比赛开始时间差值，格式：HH:mm:ss
                submitModel.setAcceptTime(DateUtils.getHHMMSSDiffOfTwoDateString(contestInfo.getStartTime(), DateUtils.getYYYYMMDDHHMMSS(new Date())));
                // 累加当前时间和比赛开始时间差值，格式：长整型，秒
                // 加上先前可能有错误的次数而罚的时间
                if (totalTime == 0) {
                    int totalErrorCount = 0;
                    for (Map.Entry<Long, SubmitModel> entry : submitMap.entrySet()) {
                        totalErrorCount += entry.getValue().getErrorCount();
                    }
                    totalTime += (totalErrorCount * ContestRankScoreRule.SECOND_TIME_PENALTY);
                }
                totalTime += DateUtils.getSecondDiffOfTwoDateString(contestInfo.getStartTime(), DateUtils.getYYYYMMDDHHMMSS(new Date()));
                // 判定是否是该题第一个提交的
                int count = submitMapper.selectCount(Wrappers.<Submit>lambdaQuery()
                        .eq(Submit::getSourceId, contestId)
                        .eq(Submit::getProblemId, problemId)
                        .eq(Submit::getSubmitStatus, SubmitStatus.ACCEPTED.getName()));
                submitModel.setFirstBlood(count == 0);
            }
        } else { // 错误答案
            // 罚时20分钟
            if (totalTime != 0) {
                totalTime += ContestRankScoreRule.SECOND_TIME_PENALTY;
            }
            submitModel.setErrorCount(submitModel.getErrorCount() + 1);
            if (solvedProblem == null) { // 尚未解决
                submitModel.setAccept(false);
                submitModel.setAcceptTime(null);
                submitModel.setFirstBlood(false);
            }
        }
        submitMap.put(problemId, submitModel);

        // key排序submitMap
        rankItemModel.setSubmitMap(sortKeyOfMap(submitMap));
        rankItemModel.setTotalTime(totalTime);

        rankItemMap.put(userId, rankItemModel);

        // value排序rankItemMap
        cacheContestRankModel.setRankItemMap(sortValueOfMap(rankItemMap));

        // 缓存有效期设置为比赛结束后一分钟
        Duration expireDuration = Duration.ofSeconds(DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime()) - new Date().getTime() / 1000 + 60);
        redisTemplate.opsForValue().set(String.format(ContestRankRedisKeyConstant.CONTEST_RANK, keyVal), cacheContestRankModel, expireDuration);

    }

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

    private void initContestRankModel(CacheContestRankModel cacheContestRankModel, Long userId) {
        Map<Long, RankItemModel> rankItemModelMap = cacheContestRankModel.getRankItemMap();
        RankItemModel rankItemModel = rankItemModelMap.get(userId);
        // map中没有该学生：该学生第一次提交评测，初始化提交统计信息与题目具体提交信息
        if (rankItemModel == null) {
            RankItemModel newRankItemModel = new RankItemModel();
            User userInfo = userService.getUserInfoById(userId);
            newRankItemModel.setUserId(userId);
            newRankItemModel.setUsername(userInfo.getUsername());
            newRankItemModel.setSubmitCount(0);
            newRankItemModel.setAcNum(0);
            newRankItemModel.setTotalTime(0l);

            Map<Long, SubmitModel> submitMap = new LinkedHashMap<>();
            List<Long> problemIds = contestService.getProblemListOfContest(cacheContestRankModel.getContestId());
            for (Long id : problemIds) {
                SubmitModel submitModel = new SubmitModel(id, false, false, null, 0);
                submitMap.put(id, submitModel);
            }
            newRankItemModel.setSubmitMap(submitMap);

            rankItemModelMap.put(userId, newRankItemModel);
        }
    }

    private Map<Long, SubmitModel> sortKeyOfMap(Map<Long, SubmitModel> map) {
        List<Map.Entry<Long, SubmitModel>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (int) (o1.getKey() - o2.getKey()));
        Map<Long, SubmitModel> ret = new LinkedHashMap<>();
        for (Map.Entry<Long, SubmitModel> entry : list) {
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    private Map<Long, RankItemModel> sortValueOfMap(Map<Long, RankItemModel> map) {
        List<Map.Entry<Long, RankItemModel>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            RankItemModel rankItemModel1 = o1.getValue();
            RankItemModel rankItemModel2 = o2.getValue();
            return (rankItemModel2.getAcNum() - rankItemModel1.getAcNum() != 0) ?
                    rankItemModel2.getAcNum() - rankItemModel1.getAcNum() : (int) (rankItemModel1.getTotalTime() - rankItemModel2.getTotalTime());
        });
        Map<Long, RankItemModel> ret = new LinkedHashMap<>();
        for (Map.Entry<Long, RankItemModel> entry : list) {
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }
}
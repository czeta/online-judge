package com.czeta.onlinejudge.consts;

/**
 * @ClassName ContestRankRedisKeyConstant
 * @Description 比赛榜单redis key常量
 * @Author chenlongjie
 * @Date 2020/3/21 16:03
 * @Version 1.0
 */
public class ContestRankRedisKeyConstant {
    /**
     * 比赛榜单model key：%s为contestId
     */
    public static final String CONTEST_RANK = "contest:rank:%s";
}

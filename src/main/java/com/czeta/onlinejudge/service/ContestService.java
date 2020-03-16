package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.ContestUser;
import com.czeta.onlinejudge.model.param.ContestModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.result.SimpleContestModel;

import java.util.List;

/**
 * @ClassName ContestService
 * @Description 比赛服务
 * @Author chenlongjie
 * @Date 2020/3/15 17:48
 * @Version 1.0
 */
public interface ContestService {

    /**
     * 创建比赛
     * @param contestModel
     * @param adminId
     */
    void saveNewContest(ContestModel contestModel, Long adminId);

    /**
     * 更新比赛信息
     * @param contestModel
     * @param contestId
     * @param adminId
     * @return
     */
    boolean updateContestInfo(ContestModel contestModel, Long contestId, Long adminId);

    /**
     * 查询比赛简略信息列表
     * @return
     */
    IPage<SimpleContestModel> getSimpleContestList(PageModel pageParam);

    /**
     * 根据比赛ID查询比赛信息
     * @param contestId
     * @return
     */
    Contest getContestInfo(Long contestId);

    /**
     * 根据比赛ID查询比赛的下属题目id列表
     * @param contestId
     * @return
     */
    List<Long> getProblemListOfContest(Long contestId);

    /**
     * 获取比赛报名用户列表
     * @param contestId
     * @return
     */
    IPage<ContestUser> getAppliedContestUserList(PageModel pageModel, Long contestId);

    /**
     * 通过or不通过用户申请
     * @param status
     * @param id
     * @return
     */
    boolean updateAppliedContestUser(Short status, Long id, Long contestId, Long adminId);
}

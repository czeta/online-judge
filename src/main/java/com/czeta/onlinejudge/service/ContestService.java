package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Announcement;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.ContestUser;
import com.czeta.onlinejudge.model.param.ContestConditionPageModel;
import com.czeta.onlinejudge.model.param.ContestModel;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.result.*;

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

    /**
     * 获取公有界面的比赛列表信息
     * @param pageModel
     * @return
     */
    IPage<PublicSimpleContestModel> getPublicContestList(PageModel pageModel);

    /**
     * 通过条件筛选比赛
     * @param contestConditionPageModel
     * @return
     */
    IPage<PublicSimpleContestModel> getPublicContestListByCondition(ContestConditionPageModel contestConditionPageModel);

    /**
     * 通过比赛ID获取比赛详情，并判断当前用户是否有权限查看其它信息（已经报名并通过的用户）
     * @param contestId
     * @param userId
     * @return
     */
    DetailContestModel getDetailContestInfoById(Long contestId, Long userId);

    /**
     * 通过比赛ID获取比赛公告列表
     * @param contestId
     * @param userId
     * @return
     */
    List<Announcement> getContestAnnouncementList(Long contestId, Long userId);

    /**
     * 通过比赛ID获取题目简略列表信息
     * @param contestId
     * @param userId
     * @return
     */
    List<PublicSimpleProblemModel> getSimpleProblemListByContestId(Long contestId, Long userId);

    /**
     * 通过问题ID获取比赛下的问题详情信息
     * @param problemId
     * @param contestId
     * @param userId
     * @return
     */
    DetailProblemModel getDetailProblemInfoByIdOfContest(Long problemId, Long contestId, Long userId);

    /**
     * 通过比赛ID分页获取比赛下的题目提交评测信息列表
     * @param pageModel
     * @param contestId
     * @param userId
     * @return
     */
    IPage<PublicSubmitModel> getSubmitModelListByContestId(PageModel pageModel, Long contestId, Long userId);

    /**
     * 通过筛选参数分页获取比赛下的题目提交评测信息列表
     * @param submitConditionPageModel
     * @return
     */
    IPage<PublicSubmitModel> getSubmitModelListByConditionOfContest(SubmitConditionPageModel submitConditionPageModel, Long contestId, Long userId);

    
}

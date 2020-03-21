package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.cache.ContestRankRedisService;
import com.czeta.onlinejudge.convert.ContestMapstructConvert;
import com.czeta.onlinejudge.convert.ProblemMapstructConvert;
import com.czeta.onlinejudge.convert.SubmitMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.*;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.*;
import com.czeta.onlinejudge.service.AnnouncementService;
import com.czeta.onlinejudge.service.ContestService;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudge.utils.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ContestServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/15 17:48
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ContestUserMapper contestUserMapper;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private ContestRankRedisService contestRankRedisService;

    @Override
    public void saveNewContest(ContestModel contestModel, Long adminId) {
        AssertUtils.isTrue(ContestSignUpRule.isContain(contestModel.getSignUpRule()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "报名模式不存在");
        AssertUtils.isTrue(ContestRankModel.isContain(contestModel.getRankModel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "排名模式不存在");
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        Contest contestInfo = ContestMapstructConvert.INSTANCE.contestModelToContest(contestModel);
        Admin admin = adminMapper.selectById(adminId);
        AssertUtils.notNull(admin, BaseStatusMsg.APIEnum.PARAM_ERROR);
        contestInfo.setCreator(admin.getUsername());
        try {
            contestMapper.insert(contestInfo);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM);
        }
    }

    @Override
    public boolean updateContestInfo(ContestModel contestModel, Long contestId, Long adminId) {
        AssertUtils.notNull(contestModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.isTrue(ContestSignUpRule.isContain(contestModel.getSignUpRule()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "报名模式不存在");
        AssertUtils.isTrue(ContestRankModel.isContain(contestModel.getRankModel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "排名模式不存在");
        Contest contestInfo = ContestMapstructConvert.INSTANCE.contestModelToContest(contestModel);
        contestInfo.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        contestMapper.updateById(contestInfo);
        return true;
    }

    @Override
    public IPage<SimpleContestModel> getSimpleContestList(PageModel pageParam) {
        Page<Contest> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Contest> contestIPage = contestMapper.selectPage(page, Wrappers.<Contest>lambdaQuery()
                .orderByAsc(Contest::getCrtTs));
        List<SimpleContestModel> list = new ArrayList<>();
        for (Contest c : contestIPage.getRecords()) {
            list.add(ContestMapstructConvert.INSTANCE.contestToSimpleContestModel(c));
        }
        return PageUtils.setOpr(contestIPage, new Page<SimpleContestModel>(), list);
    }

    @Override
    public Contest getContestInfo(Long contestId) {
        return contestMapper.selectById(contestId);
    }

    @Override
    public List<Long> getProblemListOfContest(Long contestId) {
        Contest contestInfo = getContestInfo(contestId);
        return problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                .eq(Problem::getSourceId, contestInfo.getId())
                .orderByAsc(Problem::getCrtTs))
                .stream()
                .map(Problem::getId)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<ContestUser> getAppliedContestUserList(PageModel pageParam, Long contestId) {
        Page<ContestUser> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        return contestUserMapper.selectPage(page, Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .orderByAsc(ContestUser::getCrtTs));
    }

    @Override
    public boolean updateAppliedContestUser(Short status, Long id, Long contestId, Long adminId) {
        ContestUser contestUser = new ContestUser();
        contestUser.setId(id);
        contestUser.setStatus(status);
        contestUser.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        contestUserMapper.updateById(contestUser);
        return true;
    }

    @Override
    public IPage<PublicSimpleContestModel> getPublicContestList(PageModel pageParam) {
        Page<Contest> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Contest> contestIPage = contestMapper.selectPage(page, Wrappers.<Contest>lambdaQuery()
                .eq(Contest::getStatus, CommonItemStatus.ENABLE.getCode())
                .orderByDesc(Contest::getStartTime));
        List<PublicSimpleContestModel> list = new ArrayList<>();
        for (Contest c : contestIPage.getRecords()) {
            PublicSimpleContestModel publicSimpleContestModel = ContestMapstructConvert.INSTANCE.contestToPublicSimpleContestModel(c);
            list.add(publicSimpleContestModel);
        }
        return PageUtils.setOpr(contestIPage, new Page<PublicSimpleContestModel>(), list);
    }

    @Override
    public IPage<PublicSimpleContestModel> getPublicContestListByCondition(ContestConditionPageModel contestConditionPageModel) {
        AssertUtils.notNull(contestConditionPageModel.getPageModel(), BaseStatusMsg.APIEnum.PARAM_ERROR, "无分页参数");
        List<Long> contestIds;
        if (contestConditionPageModel.getContestRunningStatus() != null) {
            Long currentTime = DateUtils.getUnixTimeOfSecond(DateUtils.getYYYYMMDDHHMMSS(new Date()));
            String runningStatus = contestConditionPageModel.getContestRunningStatus();
            if (runningStatus.equals(ContestRunningStatus.BEFORE_RUNNING.getMessage())) {
                contestIds = contestMapper.selectContestIdsBeforeRunning(currentTime);
            } else if (runningStatus.equals(ContestRunningStatus.RUNNING.getMessage())) {
                contestIds = contestMapper.selectContestIdsRunning(currentTime);
            } else if (runningStatus.equals(ContestRunningStatus.AFTER_RUNNING.getMessage())) {
                contestIds = contestMapper.selectContestIdsAfterRunning(currentTime);
            } else {
                throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR, "状态不合法");
            }
        } else {
            contestIds = contestMapper.selectList(Wrappers.<Contest>lambdaQuery()
                    .eq(Contest::getStatus, CommonItemStatus.ENABLE.getCode()))
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());;
        }
        String titleKey = contestConditionPageModel.getTitleKey();
        if (titleKey == null) {
            titleKey = "";
        }
        List<String> signUpRule = Arrays.asList(contestConditionPageModel.getSignUpRule());
        if (signUpRule == null) {
            signUpRule = ContestSignUpRule.getEnumMessageList();
        }
        List<String> rankModel = Arrays.asList(contestConditionPageModel.getRankModel());
        if (rankModel == null) {
            rankModel = ContestRankModel.getEnumMessageList();
        }
        Page<Contest> page = new Page<>(contestConditionPageModel.getPageModel().getOffset(), contestConditionPageModel.getPageModel().getLimit());
        IPage<Contest> contestIPage = contestMapper.selectPage(page, Wrappers.<Contest>lambdaQuery()
                .eq(Contest::getStatus, CommonItemStatus.ENABLE.getCode())
                .in(Contest::getSignUpRule, signUpRule)
                .in(Contest::getRankModel, rankModel)
                .in(Contest::getId, contestIds)
                .like(Contest::getTitle, "%" + titleKey + "%"));
        List<PublicSimpleContestModel> list = new ArrayList<>();
        for (Contest c : contestIPage.getRecords()) {
            PublicSimpleContestModel publicSimpleContestModel = ContestMapstructConvert.INSTANCE.contestToPublicSimpleContestModel(c);
            list.add(publicSimpleContestModel);
        }
        return PageUtils.setOpr(contestIPage, new Page<PublicSimpleContestModel>(), list);
    }

    @Override
    public DetailContestModel getDetailContestInfoById(Long contestId, Long userId) {
        Contest contestInfo = contestMapper.selectById(contestId);
        AssertUtils.notNull(contestInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "比赛不存在或已下线");
        DetailContestModel detailContestModel = ContestMapstructConvert.INSTANCE.contestToDetailContestModel(contestInfo);
        ContestUser contestUser = contestUserMapper.selectOne(Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .eq(ContestUser::getUserId, userId)
                .eq(ContestUser::getStatus, CommonReviewItemStatus.PASS.getCode()));
        detailContestModel.setPermission(contestUser != null);
        Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
        Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
        detailContestModel.setAfterStart(startTime <= currentTime);
        return detailContestModel;
    }

    @Override
    public List<Announcement> getContestAnnouncementList(Long contestId, Long userId) {
        return announcementService.getContestAnnouncementList(contestId);
    }

    @Override
    public List<PublicSimpleProblemModel> getSimpleProblemListByContestId(Long contestId, Long userId) {
        List<Problem> problemList =  problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                .eq(Problem::getSourceId, contestId)
                .orderByAsc(Problem::getCrtTs));
        List<PublicSimpleProblemModel> publicSimpleProblemModels = new ArrayList<>();
        for (Problem p : problemList) {
            PublicSimpleProblemModel problemModel = ProblemMapstructConvert.INSTANCE.problemToPublicSimpleProblemModel(p);
            problemModel.setLevel(null);
            publicSimpleProblemModels.add(problemModel);
        }
        return publicSimpleProblemModels;
    }

    @Override
    public DetailProblemModel getDetailProblemInfoByIdOfContest(Long problemId, Long contestId, Long userId) {
        DetailProblemModel detailProblemModel = problemService.getDetailProblemInfoById(problemId, false);
        detailProblemModel.setLevel(null);
        detailProblemModel.setTagList(null);
        return detailProblemModel;
    }

    @Override
    public IPage<PublicSubmitModel> getSubmitModelListByContestId(PageModel pageModel, Long contestId, Long userId) {
        Page<Submit> page = new Page<>(pageModel.getOffset(), pageModel.getLimit());
        IPage<Submit> submitIPage = submitMapper.selectPage(page, Wrappers.<Submit>lambdaQuery()
                .eq(Submit::getSourceId, contestId)
                .orderByDesc(Submit::getCrtTs));
        List<PublicSubmitModel> list = new ArrayList<>();
        for (Submit s : submitIPage.getRecords()) {
            PublicSubmitModel publicSubmitModel = SubmitMapstructConvert.INSTANCE.submitToPublicSubmitModel(s);
            publicSubmitModel.setCodeLength(s.getCode().length());
            list.add(publicSubmitModel);
        }
        return PageUtils.setOpr(submitIPage, new Page<PublicSubmitModel>(), list);
    }

    @Override
    public IPage<PublicSubmitModel> getSubmitModelListByConditionOfContest(SubmitConditionPageModel submitConditionPageModel, Long contestId, Long userId) {
        AssertUtils.notNull(submitConditionPageModel.getPageModel(), BaseStatusMsg.APIEnum.PARAM_ERROR, "无分页参数");
        List<Long> problemIds = Arrays.asList(submitConditionPageModel.getProblemId());
        if (submitConditionPageModel.getProblemId() == null) {
            problemIds = problemMapper.selectList(Wrappers.<Problem>lambdaQuery())
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());
        }
        String creatorKey = submitConditionPageModel.getCreatorKey();
        if (creatorKey == null) {
            creatorKey = "";
        }
        List<String> submitStatuses = Arrays.asList(submitConditionPageModel.getSubmitStatus());
        if (submitConditionPageModel.getSubmitStatus() == null) {
            submitStatuses = SubmitStatus.getEnumNameList();
        }
        Page<Submit> page = new Page<>(submitConditionPageModel.getPageModel().getOffset(), submitConditionPageModel.getPageModel().getLimit());
        IPage<Submit> submitIPage = submitMapper.selectPage(page, Wrappers.<Submit>lambdaQuery()
                .eq(Submit::getSourceId, contestId)
                .in(Submit::getProblemId, problemIds)
                .in(Submit::getSubmitStatus, submitStatuses)
                .like(Submit::getCreator, "%" + creatorKey + "%"));
        List<PublicSubmitModel> list = new ArrayList<>();
        for (Submit s : submitIPage.getRecords()) {
            PublicSubmitModel publicSubmitModel = SubmitMapstructConvert.INSTANCE.submitToPublicSubmitModel(s);
            publicSubmitModel.setCodeLength(s.getCode().length());
            list.add(publicSubmitModel);
        }
        return PageUtils.setOpr(submitIPage, new Page<PublicSubmitModel>(), list);
    }

    @Override
    public void submitProblemOfContest(SubmitModel submitModel, Long contestId, Long userId) {
        // 正常提交问题的过程
        problemService.submitProblem(submitModel, userId);

        /**<= 提交问题涉及到榜单实时计算的过程(这部分会统一放在评测服务中，目的是为了避免修改同一个cache key) begin =>**/
        // 该比赛的第一次提交，且redis中无数据：初始化该比赛缓存:
        // 这里与下面注释一致，将这部分推迟到评测结果出来后再执行

        // 用于mock数据的前提
        int count = submitMapper.selectCount(Wrappers.<Submit>lambdaQuery()
                .eq(Submit::getSourceId, contestId));
        if (count <= 1 && !contestRankRedisService.exists(contestId)) {
            contestRankRedisService.initContestRankRedis(contestId, submitModel.getProblemId(), userId);
            return;
        }
        // 非第一次提交，取出缓存并实时计算再存入：
        // 值得注意的是如果这里操作redis key，另外一个微服务也操作，就涉及到并发丢失修改的问题了，需要用到分布式锁，为了减少不必要的工作量，这里将实时计算推迟到评测题目有结果后。

        // 这里可以mock一些测试数据，来用来榜单展示功能测试用
        contestRankRedisService.mockRankData(contestId, submitModel.getProblemId(), userId);
        /**<= 提交问题涉及到榜单实时计算的过程(这部分会统一放在评测服务中，目的是为了避免修改同一个cache key) end =>**/
    }
}

package com.czeta.onlinejudge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.cache.ContestRankRedisService;
import com.czeta.onlinejudge.cache.model.CacheContestRankModel;
import com.czeta.onlinejudge.cache.model.RankItemModel;
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
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudge.utils.utils.NumberUtils;
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
    private MessageMapper messageMapper;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private ContestRankRedisService contestRankRedisService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContestRankMapper contestRankMapper;

    @Autowired
    private UserCertificationMapper userCertificationMapper;

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
        log.info("contestInfo={}", contestInfo);
        log.info("contestId={}", contestId);
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
        // 审核结果发送给用户
        Contest contestInfo = getContestInfo(contestId);
        Admin adminInfo = adminMapper.selectById(adminId);
        Message message = new Message();
        message.setTitle("比赛报名审核结果");
        String result = status == CommonReviewItemStatus.PASS.getCode() ?
                CommonReviewItemStatus.PASS.getMessage() : CommonReviewItemStatus.NO_PASS.getMessage();
        message.setContent("申请报名的比赛标题：" + contestInfo.getTitle() + "\n审核结果：" + result + "\n审核人：" + adminInfo.getUsername());
        message.setCreator(adminInfo.getUsername());
        message.setUserId(id);
        messageMapper.insert(message);
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
        if (CollectionUtils.isEmpty(contestIds)) {
            return new Page<>();
        }
        String titleKey = contestConditionPageModel.getTitleKey();
        if (titleKey == null) {
            titleKey = "";
        }
        List<String> signUpRule = Arrays.asList(contestConditionPageModel.getSignUpRule());
        if (contestConditionPageModel.getSignUpRule() == null) {
            signUpRule = ContestSignUpRule.getEnumMessageList();
        }
        List<String> rankModel = Arrays.asList(contestConditionPageModel.getRankModel());
        if (contestConditionPageModel.getRankModel() == null) {
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
    public Boolean saveNewSignUpContest(Long contestId, String password, Long userId) {
        // 校验：是否已经过了报名时间（比赛开始后）
        Contest contestInfo = getContestInfo(contestId);
        Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
        Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
        Long endTime = DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime());
        AssertUtils.isTrue(currentTime < startTime, BaseStatusMsg.INVALID_SIGN_UP);
        // 校验：是否已经报名
        ContestUser contestUser = contestUserMapper.selectOne(Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .eq(ContestUser::getUserId, userId));
        AssertUtils.isNull(contestUser, BaseStatusMsg.EXISTED_SIGN_UP_CONTEST);
        // 校验：比赛是否存在
        AssertUtils.notNull(contestInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "比赛不存在");
        contestUser = new ContestUser();
        contestUser.setUserId(userId);
        contestUser.setContestId(contestId);
        if (contestInfo.getSignUpRule().equals(ContestSignUpRule.PUBLIC.getMessage())) {
            contestUser.setStatus(CommonReviewItemStatus.PASS.getCode());
        } else if (contestInfo.getSignUpRule().equals(ContestSignUpRule.PASSWORD.getMessage())) {
            if (contestInfo.getPassword().equals(password)) {
                contestUser.setStatus(CommonReviewItemStatus.PASS.getCode());
            } else {
                throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR, "密码错误");
            }
        } else if (contestInfo.getSignUpRule().equals(ContestSignUpRule.CERTIFICATION.getMessage())) {
            UserCertification info = userCertificationMapper.selectOne(Wrappers.<UserCertification>lambdaQuery()
                    .eq(UserCertification::getUserId, userId)
                    .eq(UserCertification::getStatus, CommonReviewItemStatus.PASS.getCode()));
            AssertUtils.notNull(info, BaseStatusMsg.APIEnum.PARAM_ERROR, "尚未认证实名信息");
            contestUser.setStatus(CommonReviewItemStatus.TO_REVIEW.getCode());
        }
        contestUserMapper.insert(contestUser);
        return true;
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
            problemModel.setAcRate(NumberUtils.parsePercent(p.getAcCount(), p.getSubmitCount()));
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
        long submitId = problemService.submitProblem(submitModel, userId);

        /**<= 提交问题涉及到榜单实时计算的过程(这部分会统一放在评测服务中，目的是为了避免修改同一个cache key) begin =>**/
        // 该比赛的第一次提交，且redis中无数据：初始化该比赛缓存:
        // 这里与下面注释一致，将这部分推迟到评测结果出来后再执行

        // 用于mock数据的前提
        Contest contestInfo = getContestInfo(contestId);
        Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
        Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
        Long endTime = DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime());
        if (currentTime >= startTime && currentTime <= endTime) {
            int count = submitMapper.selectCount(Wrappers.<Submit>lambdaQuery()
                    .eq(Submit::getSourceId, contestId));
            if (count <= 1 && !contestRankRedisService.exists(contestId)) {
                log.info("count={}", count);
                contestRankRedisService.initContestRankRedis(contestId, submitModel.getProblemId(), userId);
                // 插入持久化榜单数据
                Map<Long, RankItemModel> mapModel = contestRankRedisService.getRankItemMapByContestIdFromCache(contestId);
                CacheContestRankModel cacheContestRankModel = new CacheContestRankModel();
                cacheContestRankModel.setContestId(contestId);
                cacheContestRankModel.setRankItemMap(mapModel);
                ContestRank contestRank = new ContestRank();
                contestRank.setContestId(contestId);
                contestRank.setRankJson(JSONObject.toJSONString(cacheContestRankModel));
                contestRankMapper.insert(contestRank);
            }
        }
        // 非第一次提交，取出缓存并实时计算再存入：
        // 值得注意的是如果这里操作redis key，另外一个微服务也操作，就涉及到并发丢失修改的问题了，需要用到分布式锁，为了减少不必要的工作量，这里将实时计算推迟到评测题目有结果后。

        // 这里可以mock一些测试数据，来用来榜单展示功能测试用
        // 随机概率mock两种情况
        boolean ac = false;
        if ((int) (Math.random() * 2) == 1) {
            ac = true;
        }
        log.info("ac={}", ac);
        if (currentTime >= startTime && currentTime <= endTime) {
            contestRankRedisService.refreshContestRankRedis(contestId, submitModel.getProblemId(), userId, ac);
            // 更新持久化数据
            Map<Long, RankItemModel> mapModel = contestRankRedisService.getRankItemMapByContestIdFromCache(contestId);
            CacheContestRankModel cacheContestRankModel = new CacheContestRankModel();
            cacheContestRankModel.setContestId(contestId);
            cacheContestRankModel.setRankItemMap(mapModel);
            ContestRank contestRank = new ContestRank();
            contestRank.setRankJson(JSONObject.toJSONString(cacheContestRankModel));
            contestRankMapper.update(contestRank, Wrappers.<ContestRank>lambdaQuery().eq(ContestRank::getContestId, contestId));
        }

        SubmitResultModel submitResultModel = new SubmitResultModel();
        submitResultModel.setProblemId(submitModel.getProblemId());
        submitResultModel.setSubmitId(submitId);
        submitResultModel.setSubmitStatus(ac ? SubmitStatus.ACCEPTED.getName() : SubmitStatus.WRONG_ANSWER.getName());
        submitResultModel.setMemory("2000kb");
        submitResultModel.setTime("2000ms");
        problemService.refreshSubmitProblem(submitResultModel, userId);
        /**<= 提交问题涉及到榜单实时计算的过程(这部分会统一放在评测服务中，目的是为了避免修改同一个cache key) end =>**/
    }

    @Override
    public IPage<RankItemModel> getRankItemListByContestId(PageModel pageModel, Long contestId, Long userId) {
        IPage<RankItemModel> itemModelIPage = new Page<>();
        Contest contestInfo = getContestInfo(contestId);
        // 报名用户列表
        Page<ContestUser> contestUserPage = new Page<>(pageModel.getOffset(), pageModel.getLimit());
        IPage<ContestUser> contestUserIPage = contestUserMapper.selectPage(contestUserPage, Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .eq(ContestUser::getStatus, CommonReviewItemStatus.PASS.getCode()));
        List<RankItemModel> rankItemModelList = new ArrayList<>();
        for (ContestUser contestUser : contestUserIPage.getRecords()) {
            RankItemModel rankItemModel = new RankItemModel();
            rankItemModel.setUserId(contestUser.getUserId());
            rankItemModel.setUsername(userService.getUserInfoById(contestUser.getUserId()).getUsername());
            rankItemModel.setSubmitCount(0);
            rankItemModel.setTotalTime(0l);
            rankItemModel.setAcNum(0);
            rankItemModel.setSubmitMap(null);
            rankItemModelList.add(rankItemModel);
        }
        // 持久化排名数据
        ContestRank contestRank = contestRankMapper.selectOne(Wrappers.<ContestRank>lambdaQuery()
                .eq(ContestRank::getContestId, contestId));
        CacheContestRankModel fromJsonToCacheModel = JSONObject.toJavaObject((JSONObject) JSONObject.parse(contestRank.getRankJson()), CacheContestRankModel.class);

        Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
        Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
        Long endTime = DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime());
        // 非实时排名
        if (contestInfo.getRealtimeRank().equals(CommonItemStatus.DISABLE.getCode())) {
            // 比赛进行中：返回报名用户列表
            if (currentTime >= startTime && currentTime <= endTime) {
                itemModelIPage = PageUtils.setOpr(contestUserIPage, new Page<RankItemModel>(), rankItemModelList);
            // 比赛结束后：从持久层中取出排名列表
            } else if (currentTime > endTime) {
                itemModelIPage = convertMapToPage(fromJsonToCacheModel.getRankItemMap(), pageModel);
            }
            return itemModelIPage;
        }
        // 实时排名
        Map<Long, RankItemModel> cacheRankItemMap = contestRankRedisService.getRankItemMapByContestIdFromCache(contestId);
        if (currentTime >= startTime && currentTime <= endTime) {
            // 比赛进行中但缓存没数据：返回报名用户列表
            if (cacheRankItemMap == null) {
                itemModelIPage = PageUtils.setOpr(contestUserIPage, new Page<RankItemModel>(), rankItemModelList);
            // 比赛进行中缓存有数据：返回缓存数据
            } else {
                itemModelIPage = convertMapToPage(cacheRankItemMap, pageModel);
            }
        // 比赛结束后：从持久层中取出排名列表
        } else if (currentTime > endTime) {
            itemModelIPage = convertMapToPage(fromJsonToCacheModel.getRankItemMap(), pageModel);
        }
        return itemModelIPage;
    }

    private IPage<RankItemModel> convertMapToPage(Map<Long, RankItemModel> map, PageModel pageModel) {
        if (pageModel.getLimit() == 0 || pageModel.getOffset() == 0) return new Page<>();
        List<RankItemModel> list = new ArrayList<>();
        for (Map.Entry<Long, RankItemModel> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        int begin = (pageModel.getOffset() - 1) * pageModel.getLimit();
        int end = Math.min(pageModel.getOffset() * pageModel.getLimit(), list.size());
        List<RankItemModel> newList = list.subList(begin, end);
        IPage<RankItemModel> rankItemModelIPage = new Page<>();
        rankItemModelIPage.setRecords(newList);
        rankItemModelIPage.setTotal(list.size());
        rankItemModelIPage.setSize(pageModel.getLimit());
        rankItemModelIPage.setCurrent(pageModel.getOffset());
        rankItemModelIPage.setPages(list.size() / pageModel.getLimit() + (list.size() % pageModel.getLimit() == 0 ? 0 : 1));
        return rankItemModelIPage;
    }

}

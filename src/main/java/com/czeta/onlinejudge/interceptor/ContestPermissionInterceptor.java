package com.czeta.onlinejudge.interceptor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.ContestUser;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.ContestMapper;
import com.czeta.onlinejudge.dao.mapper.ContestUserMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.CommonReviewItemStatus;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName ContestPermissionInterceptor
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/16 11:26
 * @Version 1.0
 */
@Slf4j
@Component
@Aspect
public class ContestPermissionInterceptor {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private ContestUserMapper contestUserMapper;

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.update*(..))")
    public void updateContestInfo() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.AnnouncementService.updateContestAnnouncement(..))")
    public void updateContestAnnouncement() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.AnnouncementService.saveNewContestAnnouncement(..))")
    public void saveContestAnnouncement() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.calculateRatingDataOfRatingContest(..))")
    public void calculateRatingDataOfRatingContest() {}

    /**
     * 对比赛信息管理员操作的功能进行权限拦截：只有创建指定比赛的管理员才有对该比赛的操作权限
     * @param joinPoint
     */
    @Before("updateContestInfo() || updateContestAnnouncement() || saveContestAnnouncement() || calculateRatingDataOfRatingContest()")
    public void beforeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        // 获取参数下标
        int contestIdIndex = ArrayUtils.indexOf(parameterNames, "contestId");
        // 获取groupAppId的下标
        int adminIdIndex = ArrayUtils.indexOf(parameterNames, "adminId");
        if (contestIdIndex == -1 || adminIdIndex == -1) {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR);
        }
        Object[] args = joinPoint.getArgs();
        Long contestId = (Long) args[contestIdIndex];
        Long adminId = (Long) args[adminIdIndex];
        Admin adminInfo = adminMapper.selectById(adminId);
        AssertUtils.notNull(adminInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "管理员不存在");
        Contest contestInfo = contestMapper.selectById(contestId);
        AssertUtils.notNull(contestInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "比赛不存在");
        AssertUtils.isTrue(adminInfo.getUsername().equals(contestInfo.getCreator()), BaseStatusMsg.APIEnum.AUTHORITY_EXCEED, "没有该比赛的修改权限");
    }

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getContestAnnouncementList(..))")
    public void getContestAnnouncementList() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getSimpleProblemListByContestId(..))")
    public void getSimpleProblemListByContestId() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getDetailProblemInfoByIdOfContest(..))")
    public void getDetailProblemInfoByIdOfContest() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getSubmitModelListByContestId(..))")
    public void getSubmitModelListByContestId() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getSubmitModelListByConditionOfContest(..))")
    public void getSubmitModelListByConditionOfContest() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.submitProblemOfContest(..))")
    public void submitProblemOfContest() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.getRankItemListByContestId(..))")
    public void getRankItemListByContestId() {}

    /**
     * 对比赛详情界面的用户功能操作进行权限拦截：判定用户是否参赛+比赛已经开始
     * @param joinPoint
     */
    @Before("getContestAnnouncementList() || getSimpleProblemListByContestId() || getDetailProblemInfoByIdOfContest()" +
            "|| getSubmitModelListByContestId() || getSubmitModelListByConditionOfContest() || submitProblemOfContest() || getRankItemListByContestId()")
    public void beforeContestMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        // 获取参数下标
        int contestIdIndex = ArrayUtils.indexOf(parameterNames, "contestId");
        // 获取groupAppId的下标
        int userIdIndex = ArrayUtils.indexOf(parameterNames, "userId");
        if (contestIdIndex == -1 || userIdIndex == -1) {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR);
        }
        Object[] args = joinPoint.getArgs();
        Long contestId = (Long) args[contestIdIndex];
        Long userId = (Long) args[userIdIndex];
        // 判定比赛是否开始
        Contest contestInfo = contestMapper.selectById(contestId);
        AssertUtils.notNull(contestInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "比赛不存在");
        Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
        Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
        AssertUtils.isTrue(startTime <= currentTime, BaseStatusMsg.APIEnum.AUTHORITY_EXCEED, "没有权限：比赛尚未开始");
        // 判定该用户是否是该比赛的合格参赛者
        ContestUser contestUser = contestUserMapper.selectOne(Wrappers.<ContestUser>lambdaQuery()
                .eq(ContestUser::getContestId, contestId)
                .eq(ContestUser::getUserId, userId)
                .eq(ContestUser::getStatus, CommonReviewItemStatus.PASS.getCode()));
        AssertUtils.notNull(contestUser, BaseStatusMsg.APIEnum.AUTHORITY_EXCEED, "没有权限：未申请参赛或尚未通过参赛审核");
    }
}

package com.czeta.onlinejudge.interceptor;

import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.ContestMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Pointcut("execution(* com.czeta.onlinejudge.service.ContestService.update*(..))")
    public void updateContestInfo() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.AnnouncementService.updateContestAnnouncement(..))")
    public void updateContestAnnouncement() {}

    @Pointcut("execution(* com.czeta.onlinejudge.service.AnnouncementService.saveNewContestAnnouncement(..))")
    public void saveContestAnnouncement() {}

    @Before("updateContestInfo() || updateContestAnnouncement() || saveContestAnnouncement()")
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
}

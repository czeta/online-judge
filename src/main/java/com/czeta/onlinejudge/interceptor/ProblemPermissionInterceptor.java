package com.czeta.onlinejudge.interceptor;

import com.czeta.onlinejudge.dao.entity.Admin;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.mapper.AdminMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemMapper;
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
 * @ClassName ProblemInterceptor
 * @Description 题目修改的权限拦截；题目评测数据删除，上传的权限拦截。
 * @Author chenlongjie
 * @Date 2020/3/15 15:28
 * @Version 1.0
 */
@Slf4j
@Component
@Aspect
public class ProblemPermissionInterceptor {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private AdminMapper adminMapper;

    // 文件的操作的切点
    @Pointcut("execution(* com.czeta.onlinejudge.service.impl.ProblemServiceImpl.upload*(..))")
    public void uploadFile() {}
    @Pointcut("execution(* com.czeta.onlinejudge.service.impl.ProblemServiceImpl.remove*(..))")
    public void removeFile() {}
    // 题目信息更新的切点
    @Pointcut("execution(* com.czeta.onlinejudge.service.impl.ProblemServiceImpl.update*(..))")
    public void updateProblemInfo() {}

    /**
     * 拦截不是该用户创建的题目数据操作请求
     * @param joinPoint
     */
    @Before("uploadFile() || removeFile() || updateProblemInfo()")
    public void beforeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        // 获取参数下标
        int problemIdIndex = ArrayUtils.indexOf(parameterNames, "problemId");
        // 获取groupAppId的下标
        int adminIdIndex = ArrayUtils.indexOf(parameterNames, "adminId");
        if (problemIdIndex == -1 || adminIdIndex == -1) {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR);
        }
        Object[] args = joinPoint.getArgs();
        Long problemId = (Long) args[problemIdIndex];
        Long adminId = (Long) args[adminIdIndex];
        Admin adminInfo = adminMapper.selectById(adminId);
        AssertUtils.notNull(adminInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "管理员不存在");
        Problem problemInfo = problemMapper.selectById(problemId);
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(adminInfo.getUsername().equals(problemInfo.getCreator()), BaseStatusMsg.APIEnum.AUTHORITY_EXCEED, "没有该题目的修改权限");
    }
}

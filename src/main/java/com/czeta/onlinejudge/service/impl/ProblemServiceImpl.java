package com.czeta.onlinejudge.service.impl;

import com.czeta.onlinejudge.convert.ProblemMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.ProblemJudgeType;
import com.czeta.onlinejudge.dao.entity.ProblemTag;
import com.czeta.onlinejudge.dao.mapper.ProblemJudgeTypeMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemTagMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.model.param.MachineProblemModel;
import com.czeta.onlinejudge.model.param.SpiderProblemModel;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ProblemServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 12:29
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemTagMapper problemTagMapper;

    @Autowired
    private ProblemJudgeTypeMapper problemJudgeTypeMapper;

    @Override
    public long saveNewProblemBySpider(SpiderProblemModel spiderProblemModel, Long adminId) {
        // 调用爬虫服务
        return 1;
    }

    @Override
    public long saveNewProblemByMachine(MachineProblemModel machineProblemModel, Long adminId) {
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 题目信息
        Problem problemInfo = ProblemMapstructConvert.INSTANCE.machineProblemToProblem(machineProblemModel);
        problemInfo.setCreator(adminService.getAdminInfoById(adminId).getUsername());
        try {
            problemMapper.insert(problemInfo);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM);
        }
        // 题目标签
        Long problemId = problemInfo.getId();
        ProblemTag problemTag = new ProblemTag();
        problemTag.setProblemId(problemId);
        for (Integer tagId : machineProblemModel.getTagId()) {
            problemTag.setTagId(tagId);
            problemTagMapper.insert(problemTag);
            problemTag.setId(null);
        }
        // 题目评测方式
        ProblemJudgeType problemJudgeType = ProblemMapstructConvert.INSTANCE.machineProblemToProblemJudgeType(machineProblemModel);
        problemJudgeType.setProblemId(problemId);
        try {
            problemJudgeTypeMapper.insert(problemJudgeType);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM_JUDGE_TYPE);
        }
        return problemId;
    }
}

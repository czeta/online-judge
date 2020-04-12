package com.czeta.onlinejudge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.convert.SubmitMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.SolvedProblem;
import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.dao.mapper.ProblemMapper;
import com.czeta.onlinejudge.dao.mapper.SolvedProblemMapper;
import com.czeta.onlinejudge.dao.mapper.SubmitMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.SubmitConditionPageModel;
import com.czeta.onlinejudge.model.result.PublicSubmitModel;
import com.czeta.onlinejudge.service.SubmitService;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SubmitServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:27
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class SubmitServiceImpl implements SubmitService {
    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Override
    public IPage<PublicSubmitModel> getPublicSubmitModelList(PageModel pageParam) {
        Page<Submit> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Submit> submitIPage = submitMapper.selectPage(page, Wrappers.<Submit>lambdaQuery()
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
    public IPage<PublicSubmitModel> getPublicSubmitModelListByCondition(SubmitConditionPageModel submitConditionPageModel) {
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
    public String getSubmitMsgCode(Long submitId, Long problemId, Long userId) {
        // 校验是否是自己提交的代码
        Submit submit = submitMapper.selectOne(Wrappers.<Submit>lambdaQuery()
                .eq(Submit::getId, submitId)
                .eq(Submit::getCreatorId, userId));
        // 格式为: { msg: [ { caseResult1 }, { caseResult2 } ], code: ? }
        JSONObject msgCodeJson = new JSONObject();
        if (submit != null) {
            msgCodeJson.put("msg", submit.getMsg());
            msgCodeJson.put("code", submit.getCode());
            return msgCodeJson.toJSONString();
        }
        // 校验是否有该题所有代码（其他人提交）阅读权限（已解决该题）
        SolvedProblem solvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                .eq(SolvedProblem::getProblemId, problemId)
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName()));
        AssertUtils.notNull(solvedProblem, BaseStatusMsg.APIEnum.AUTHORITY_EXCEED, "无该题代码阅读权限");
        Submit ret = submitMapper.selectOne(Wrappers.<Submit>lambdaQuery().eq(Submit::getId, submitId));
        msgCodeJson.put("msg", ret.getMsg());
        msgCodeJson.put("code", ret.getCode());
        return msgCodeJson.toJSONString();
    }
}

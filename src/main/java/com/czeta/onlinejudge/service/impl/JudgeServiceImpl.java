package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.convert.JudgeMapstructConvert;
import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.dao.mapper.JudgeTypeMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.JudgeStatus;
import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudge.model.param.JudgeTypeModel;
import com.czeta.onlinejudge.service.JudgeService;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName JudgeServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 11:27
 * @Version 1.0
 */
@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private JudgeTypeMapper judgeTypeMapper;

    @Override
    public List<JudgeType> getJudgeMachineList() {
        return judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_MACHINE.getCode())
                .orderByDesc(JudgeType::getStatus));
    }

    @Override
    public void saveNewJudgeMachine(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getName(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(judgeTypeModel.getUrl(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeType.setType(JudgeTypeEnum.JUDGE_MACHINE.getCode());
        judgeType.setStatus(JudgeStatus.STOPPED.getCode());
        judgeTypeMapper.insert(judgeType);
    }

    @Override
    public boolean updateJudgeInfoById(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeType.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        judgeTypeMapper.updateById(judgeType);
        return true;
    }

    @Override
    public List<JudgeType> getJudgeSpiderList() {
        return judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_SPIDER.getCode())
                .orderByDesc(JudgeType::getStatus));
    }

    @Override
    public void saveNewJudgeSpider(JudgeTypeModel judgeTypeModel) {
        AssertUtils.notNull(judgeTypeModel.getName(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        AssertUtils.notNull(judgeTypeModel.getUrl(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        JudgeType judgeType = JudgeMapstructConvert.INSTANCE.judgeTypeModelToJudgeType(judgeTypeModel);
        judgeType.setType(JudgeTypeEnum.JUDGE_SPIDER.getCode());
        judgeType.setStatus(JudgeStatus.STOPPED.getCode());
        judgeTypeMapper.insert(judgeType);
    }
}

package com.czeta.onlinejudge.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.dao.mapper.JudgeTypeMapper;
import com.czeta.onlinejudge.enums.JudgeServerStatus;
import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudge.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName JudgeServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/5 11:27
 * @Version 1.0
 */
@Slf4j
@Transactional
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
    public List<JudgeType> getUniqueJudgeMachineList() {
        List<JudgeType> rawList = this.getJudgeMachineList();
        Set<String> nameSet = new HashSet<>();
        List<JudgeType> ret = new ArrayList<>();
        for (JudgeType judgeType : rawList) {
            if (judgeType.getStatus().equals(JudgeServerStatus.NORMAL.getCode())
                    && !nameSet.contains(judgeType.getName())) {
                nameSet.add(judgeType.getName());
                ret.add(judgeType);
            }
        }
        return ret;
    }

    @Override
    public JudgeType getJudgeMachineById(Integer id) {
        return judgeTypeMapper.selectById(id);
    }

    @Override
    public List<JudgeType> getJudgeSpiderList() {
        return judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_SPIDER.getCode())
                .orderByDesc(JudgeType::getStatus));
    }

    @Override
    public boolean updateJudgeStatusById(Short status, Integer judgeTypeId) {
        JudgeType judgeType = new JudgeType();
        judgeType.setId(judgeTypeId);
        judgeType.setStatus(status);
        judgeTypeMapper.updateById(judgeType);
        return true;
    }
}

package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.JudgeType;

import java.util.List;

/**
 * @ClassName JudgeService
 * @Description 评测服务
 * @Author chenlongjie
 * @Date 2020/3/5 11:27
 * @Version 1.0
 */
public interface JudgeService {

    /**
     * 获取评测机列表
     * @return
     */
    List<JudgeType> getJudgeMachineList();

    /**
     * 获取去重后的NORMAL评测机列表（种类去重）
     * @return
     */
    List<JudgeType> getUniqueJudgeMachineList();

    /**
     * 通过id获取指定的评测机信息
     * @param id
     * @return
     */
    JudgeType getJudgeMachineById(Integer id);


    /**
     * 获取评测爬虫列表
     * @return
     */
    List<JudgeType> getJudgeSpiderList();

    /**
     * 更新评测状态（正常(1)/异常(-1) <-> 停用(0)）
     * @param Status
     * @param judgeTypeId
     * @return
     */
    boolean updateJudgeStatusById(Short Status, Integer judgeTypeId);
}

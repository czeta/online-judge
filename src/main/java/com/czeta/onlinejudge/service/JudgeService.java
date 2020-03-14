package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.model.param.JudgeTypeModel;

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
     * 通过id获取指定的评测机信息
     * @param id
     * @return
     */
    JudgeType getJudgeMachineById(Integer id);

    /**
     * 添加新的评测机
     * 需要新的评测机接入代码中，不然手动启用评测机，心跳会自动报abnormal，所以这里评测机默认是停止状态
     * @param judgeTypeModel
     */
    void saveNewJudgeMachine(JudgeTypeModel judgeTypeModel);

    /**
     * 获取评测爬虫列表
     * @return
     */
    List<JudgeType> getJudgeSpiderList();

    /**
     * 添加新的爬虫评测
     * 需要新的爬虫接入代码中，不然手动启用爬虫，心跳会自动报abnormal，所以这里评测机默认是停止状态
     * @param judgeTypeModel
     */
    void saveNewJudgeSpider(JudgeTypeModel judgeTypeModel);

    /**
     * 更新评测信息（评测机和爬虫）
     * @param judgeTypeModel
     * @return
     */
    boolean updateJudgeInfoById(JudgeTypeModel judgeTypeModel);
}

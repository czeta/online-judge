package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.model.param.MachineProblemModel;
import com.czeta.onlinejudge.model.param.SpiderProblemModel;

/**
 * @InterfaceName ProblemService
 * @Description 题目服务
 * @Author chenlongjie
 * @Date 2020/3/12 12:28
 * @Version 1.0
 */
public interface ProblemService {
    /**
     * 创建爬虫评测的题目，并返回创建好的题目id
     * @param spiderProblemModel
     * @param adminId
     * @return
     */
    long saveNewProblemBySpider(SpiderProblemModel spiderProblemModel, Long adminId);

    /**
     * 创建评测机评测的题目，并返回创建的题目id
     * @param machineProblemModel
     * @param adminId
     * @return
     */
    long saveNewProblemByMachine(MachineProblemModel machineProblemModel, Long adminId);
}

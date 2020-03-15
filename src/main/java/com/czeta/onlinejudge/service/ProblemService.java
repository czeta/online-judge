package com.czeta.onlinejudge.service;

import com.czeta.onlinejudge.enums.ProblemType;
import com.czeta.onlinejudge.model.param.MachineProblemModel;
import com.czeta.onlinejudge.model.param.SpiderProblemModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 上传题目评测数据：每次上传成对上传文件，包含in文件与out文件，名称必须相同
     * @param files
     * @param problemId
     * @return
     */
    boolean uploadProblemJudgeFile(MultipartFile[] files, Long problemId) throws Exception;

    /**
     * 上传题目评测数据：spj文件或insert文件（分别对应spj题型和函数型题型）
     * @param file
     * @param problemId
     * @param problemType
     * @return
     * @throws Exception
     */
    boolean uploadOtherProblemJudgeFile(MultipartFile file, Long problemId, ProblemType problemType) throws Exception;

    // end this
    /**
     * 获取评测文件名列表，分为in、out和insert.cpp，spj.cpp
     * @param problemId
     * @param problemType
     * @return
     */
    List<String> getProblemJudgeFileList(Long problemId, ProblemType problemType);

    /**
     * 通过题号定位并移除指定的评测文件
     * @param problemId
     * @param fileName
     * @return
     */
    boolean removeProblemJudgeFile(Long problemId, String fileName);

    /**
     * 查看评测机方式评测的题目信息
     * @param problemId
     * @return
     */
    MachineProblemModel getProblemInfoOfMachine(Long problemId);

    /**
     * 更新评测机方式评测的题目信息
     * @param machineProblemModel
     * @param adminId
     * @return
     */
    boolean updateProblemInfoOfMachine(MachineProblemModel machineProblemModel, Long adminId);

}

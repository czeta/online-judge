package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.enums.ProblemType;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.model.result.SimpleProblemModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
     * todo 爬虫爬取题目模块
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
     * @param adminId
     * @return
     */
    boolean uploadProblemJudgeFile(MultipartFile[] files, Long problemId, Long adminId) throws Exception;

    /**
     * 上传题目评测数据：spj文件或insert文件（分别对应spj题型和函数型题型）
     * @param file
     * @param problemId
     * @param problemType
     * @param adminId
     * @return
     * @throws Exception
     */
    boolean uploadOtherProblemJudgeFile(MultipartFile file, Long problemId, ProblemType problemType, Long adminId) throws Exception;

    /**
     * 根据文件名下载评测数据
     * @param problemId
     * @param fileName
     * @param httpServletResponse
     * @throws Exception
     */
    void downloadProblemJudgeFile(Long problemId, String fileName, HttpServletResponse httpServletResponse) throws Exception;

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
     * @param adminId
     * @return
     */
    boolean removeProblemJudgeFile(Long problemId, String fileName, Long adminId);

    /**
     * 查看评测机方式评测的题目信息
     * @param problemId
     * @return
     */
    MachineProblemModel getProblemInfoOfMachine(Long problemId);

    /**
     * 更新评测机方式评测的题目信息
     * @param machineProblemModel
     * @param userId
     * @return
     */
    boolean updateProblemInfoOfMachine(MachineProblemModel machineProblemModel, Long problemId, Long userId);

    /**
     * 获得题目简略列表信息
     * @param pageModel
     * @return
     */
    IPage<SimpleProblemModel> getSimpleProblemList(PageModel pageModel);

    /**
     * 获取公有界面的题目列表信息
     * @param pageModel
     * @return
     */
    IPage<PublicSimpleProblemModel> getPublicProblemList(PageModel pageModel);

    /**
     * 通过条件筛选题目
     * @param problemConditionPageModel
     * @return
     */
    IPage<PublicSimpleProblemModel> getPublicProblemListByCondition(ProblemConditionPageModel problemConditionPageModel);

    /**
     * 通过题号获取题目详细信息
     */
    DetailProblemModel getDetailProblemInfoById(Long problemId, Boolean visible);

    /**
     * 提交题目
     * @param submitModel
     * @param userId
     * todo kafka消息模块
     */
    void submitProblem(SubmitModel submitModel, Long userId);
}

package com.czeta.onlinejudge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 上传题目评测数据：zip文件格式，
     * 其中文件名规则：
     * (1)如果不是spj，则文件名需要成对出现，并且文件名一致（从1开始递增），后缀分别是in和out；
     * (2)如果是则文件名单个出现，并且文件名从1开始递增，后缀是in
     * @param file
     * @param spj
     * @param problemId
     * @param adminId
     * @return
     */
    boolean uploadTestCaseZip(MultipartFile file, Boolean spj, Long problemId, Long adminId) throws Exception;

    /**
     * 下载评测数据zip文件
     * @param problemId
     * @param httpServletResponse
     * @throws Exception
     */
    void downloadTestCaseZip(Long problemId, HttpServletResponse httpServletResponse) throws Exception;

    /**
     * 获取评测文件名列表
     * @param problemId
     * @return
     */
    List<String> getTestCaseFileList(Long problemId);

    /**
     * 查看评测机/爬虫方式评测的题目信息
     * @param problemId
     * @return
     */
    MachineProblemModel getProblemInfo(Long problemId);

    /**
     * 更新评测机方式评测的题目信息
     * @param machineProblemModel
     * @param userId
     * @return
     */
    boolean updateProblemInfoOfMachine(MachineProblemModel machineProblemModel, Long problemId, Long userId);

    /**
     * 更新题目状态
     * @param status
     * @param problemId
     * @param adminId
     * @return
     */
    boolean updateProblemStatus(Short status, Long problemId, Long adminId);

    /**
     * 获得题目简略列表信息
     * @param pageModel
     * @return
     */
    IPage<SimpleProblemModel> getSimpleProblemList(PageModel pageModel);

    /**
     * 根据题目关键字获取题目简略列表信息
     * @param pageModel
     * @return
     */
    IPage<SimpleProblemModel> getSimpleProblemListByTitleKey(PageModel<String> pageModel);

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
     * 提交题目，并返回提交评测ID
     * @param submitModel
     * @param userId
     */
    long submitProblem(SubmitModel submitModel, Long userId);
}

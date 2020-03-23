package com.czeta.onlinejudge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.consts.FileConstant;
import com.czeta.onlinejudge.convert.ProblemMapstructConvert;
import com.czeta.onlinejudge.convert.SubmitMapstructConvert;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.mapper.*;
import com.czeta.onlinejudge.enums.*;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.model.result.SimpleProblemModel;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.service.TagService;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private MultipartProperties multipartProperties;

    @Autowired
    private TagService tagService;

    @Autowired
    private JudgeTypeMapper judgeTypeMapper;

    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Autowired
    private UserService userService;

    @Override
    public long saveNewProblemBySpider(SpiderProblemModel spiderProblemModel, Long adminId) {
        /**<== 调用爬虫服务爬取目标题目 begin ==>**/
        /**<== 调用爬虫服务爬取目标题目 end ==>**/
        return 1;
    }

    @Override
    public long saveNewProblemByMachine(MachineProblemModel machineProblemModel, Long adminId) {
        // 校验：管理员id是否合法
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 校验：题目的水平与语言是否是合法的
        AssertUtils.isTrue(ProblemLevel.isContainMessage(machineProblemModel.getLevel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "题目难度不合法或不支持");
        AssertUtils.isTrue(ProblemLanguage.isContainMessage(new ArrayList<>(Arrays.asList(machineProblemModel.getLanguage().split(",")))),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "题目语言不合法或不支持");
        // 题目信息
        Problem problemInfo = ProblemMapstructConvert.INSTANCE.machineProblemToProblem(machineProblemModel);
        problemInfo.setLanguage(machineProblemModel.getLanguage());
        problemInfo.setCreator(adminService.getAdminInfoById(adminId).getUsername());
        try {
            problemMapper.insert(problemInfo);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public boolean uploadProblemJudgeFile(MultipartFile[] files, Long problemId, Long adminId) throws Exception {
        // 校验：是否只有两个文件
        AssertUtils.isTrue(files.length == 2, BaseStatusMsg.APIEnum.PARAM_ERROR, "文件个数不正确");
        // 校验：两个文件名称是否相同，并且后缀一个是in，一个是out
        String fileName0 = files[0].getOriginalFilename();
        String fileName1 = files[1].getOriginalFilename();
        String fileExtension0 = FilenameUtils.getExtension(fileName0);
        String fileExtension1 = FilenameUtils.getExtension(fileName1);
        AssertUtils.isTrue(FilenameUtils.getBaseName(fileName0).equals(FilenameUtils.getBaseName(fileName1)),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "文件名不一致");
        Map<String, Integer> ext = new HashMap<String, Integer>() {{
            put(FileConstant.SUFFIX_EXTENSION_IN, 0);
            put(FileConstant.SUFFIX_EXTENSION_OUT, 0);
        }};
        ext.put(fileExtension0, 1);
        ext.put(fileExtension1, 1);
        AssertUtils.isTrue(ext.get(FileConstant.SUFFIX_EXTENSION_IN) == 1 && ext.get(FileConstant.SUFFIX_EXTENSION_OUT) == 1,
                BaseStatusMsg.APIEnum.PARAM_ERROR, "文件后缀名不正确");
        // 校验：题目ID是否存在
        Problem problemInfo = problemMapper.selectById(problemId);
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目尚未创建");
        // 获取已有文件，并按顺序重命名上传的文件名
        File judgeDataDir = new File(multipartProperties.getUploadJudgeFileData() + problemId);
        if (!judgeDataDir.exists()) {
            boolean flag = judgeDataDir.mkdirs();
            if (!flag) {
                log.error("上传文件中创建{}目录失败", judgeDataDir);
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.FAILED);
            }
            fileName0 = "1." + fileExtension0;
            fileName1 = "1." + fileExtension1;
        } else {
            Set<Integer> set = new HashSet<>();
            for (File f : judgeDataDir.listFiles()) {
                String extName = FilenameUtils.getExtension(f.getName());
                if (extName.equals(FileConstant.SUFFIX_EXTENSION_IN) || extName.equals(FileConstant.SUFFIX_EXTENSION_OUT)) {
                    set.add(Integer.valueOf(FilenameUtils.getBaseName(f.getName())));
                }
            }
            int no = Collections.max(set) + 1;
            fileName0 = no + "." + fileExtension0;
            fileName1 = no + "." + fileExtension1;
        }
        // 上传文件
        final String f0 = fileName0;
        final String f1 = fileName1;
        String saveFileName0 = UploadUtils.upload(multipartProperties.getUploadJudgeFileData() + problemId, files[0],
                originalFilename -> {
                    return f0;
                }, multipartProperties.getAllowUploadFileExtensions());
        String saveFileName1 = UploadUtils.upload(multipartProperties.getUploadJudgeFileData() + problemId, files[1],
                originalFilename -> {
                    return f1;
                }, multipartProperties.getAllowUploadFileExtensions());
        log.info("saveFileName0={}, saveFileName1={}", saveFileName0, saveFileName1);
        return true;
    }

    @Override
    public boolean uploadOtherProblemJudgeFile(MultipartFile file, Long problemId, ProblemType problemType, Long adminId) throws Exception {
        // 校验：题目ID是否存在
        Problem problemInfo = problemMapper.selectById(problemId);
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目尚未创建");
        // 校验：文件名是否复合
        String fileName = file.getOriginalFilename();
        if (problemType.getCode().equals(ProblemType.FUNCTION.getCode())) {
            AssertUtils.isTrue(FileConstant.JUDGE_INSERT_NAME.equals(fileName), BaseStatusMsg.APIEnum.PARAM_ERROR, "文件名不正确");
        } else if (problemType.getCode().equals(ProblemType.SPJ.getCode())) {
            AssertUtils.isTrue(FileConstant.JUDGE_SPJ_NAME.equals(fileName), BaseStatusMsg.APIEnum.PARAM_ERROR,"文件名不正确");
        } else {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR, "文件名不正确");
        }
        // 校验：文件夹中是否已经存在该文件
        File judgeDataDir = new File(multipartProperties.getUploadJudgeFileData() + problemId);
        if (!judgeDataDir.exists()) {
            boolean flag = judgeDataDir.mkdirs();
            if (!flag) {
                log.error("上传文件中创建{}目录失败", judgeDataDir);
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.FAILED);
            }
        } else {
            for (File f : judgeDataDir.listFiles()) {
                if (fileName.equals(f.getName())) {
                    throw new APIRuntimeException(BaseStatusMsg.APIEnum.PARAM_ERROR, "文件已存在");
                }
            }
        }
        String saveFileName = UploadUtils.upload(multipartProperties.getUploadJudgeFileData() + problemId, file,
                originalFilename -> {
                    return fileName;
                }, multipartProperties.getAllowUploadFileExtensions());
        log.info("saveFileName={}", saveFileName);
        return true;
    }

    @Override
    public void downloadProblemJudgeFile(Long problemId, String fileName, HttpServletResponse response) throws Exception {
        String downloadDir = multipartProperties.getUploadJudgeFileData() + problemId;
        File targetFile = new File(downloadDir + "/" + fileName);
        AssertUtils.isTrue(targetFile.exists(), BaseStatusMsg.APIEnum.PARAM_ERROR, "题目所在文件夹不存在或该文件不存在");
        List<String> allowFileExtensions = multipartProperties.getAllowDownloadFileExtensions();
        DownloadUtils.download(downloadDir, fileName, allowFileExtensions, response, (dir, fileName0, file, fileExtension, contentType, length) -> {
            return true;
        });
    }

    @Override
    public List<String> getProblemJudgeFileList(Long problemId, ProblemType problemType) {
        File judgeDataDir = new File(multipartProperties.getUploadJudgeFileData() + problemId);
        AssertUtils.isTrue(judgeDataDir.exists(), BaseStatusMsg.APIEnum.PARAM_ERROR, "题目所在文件夹不存在");
        List<String> fileNameList = new ArrayList<>();
        if (problemType.getCode().equals(ProblemType.FUNCTION.getCode())) {
            for (File f : judgeDataDir.listFiles()) {
                if (f.getName().equals(FileConstant.JUDGE_INSERT_NAME)) {
                    fileNameList.add(f.getName());
                }
            }
        } else if (problemType.getCode().equals(ProblemType.SPJ.getCode())) {
            for (File f : judgeDataDir.listFiles()) {
                if (f.getName().equals(FileConstant.JUDGE_SPJ_NAME)) {
                    fileNameList.add(f.getName());
                }
            }
        } else {
            for (File f : judgeDataDir.listFiles()) {
                if (FilenameUtils.getExtension(f.getName()).equals(FileConstant.SUFFIX_EXTENSION_IN)
                        || FilenameUtils.getExtension(f.getName()).equals(FileConstant.SUFFIX_EXTENSION_OUT)) {
                    fileNameList.add(f.getName());
                }
            }
        }
        return fileNameList;
    }

    @Override
    public boolean removeProblemJudgeFile(Long problemId, String fileName, Long adminId) {
        String pathName = multipartProperties.getUploadJudgeFileData() + problemId;
        File targetFile = new File(pathName + "/" + fileName);
        AssertUtils.isTrue(targetFile.exists(), BaseStatusMsg.APIEnum.PARAM_ERROR, "题目所在文件夹不存在或该文件不存在");
        targetFile.delete();
        return true;
    }

    @Override
    public MachineProblemModel getProblemInfoOfMachine(Long problemId) {
        MachineProblemModel machineProblemModel = problemMapper.selectProblemJoinJudgeType(problemId);
        return machineProblemModel;
    }

    @Override
    public boolean updateProblemInfoOfMachine(MachineProblemModel machineProblemModel, Long problemId, Long adminId) {
        // 校验：题目ID
        AssertUtils.notNull(machineProblemModel.getId(), BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 校验：管理员id是否合法
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 校验：题目的水平与语言是否是合法的
        AssertUtils.isTrue(ProblemLevel.isContainMessage(machineProblemModel.getLevel()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "题目难度不合法或不支持");
        AssertUtils.isTrue(ProblemLanguage.isContainMessage(new ArrayList<>(Arrays.asList(machineProblemModel.getLanguage().split(",")))),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "题目语言不合法或不支持");
        // 更新题目信息
        Problem problemInfo = ProblemMapstructConvert.INSTANCE.machineProblemToProblem(machineProblemModel);
        problemInfo.setLanguage(machineProblemModel.getLanguage());
        problemInfo.setCreator(adminService.getAdminInfoById(adminId).getUsername());
        problemInfo.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        problemMapper.updateById(problemInfo);
        // 更新题目标签
        problemTagMapper.delete(Wrappers.<ProblemTag>lambdaQuery()
                .eq(ProblemTag::getProblemId, problemInfo.getId()));
        ProblemTag problemTag = new ProblemTag();
        problemTag.setProblemId(problemInfo.getId());
        for (Integer tagId : machineProblemModel.getTagId()) {
            problemTag.setTagId(tagId);
            problemTagMapper.insert(problemTag);
            problemTag.setId(null);
        }
        // 更新题目评测方式
        ProblemJudgeType problemJudgeType = ProblemMapstructConvert.INSTANCE.machineProblemToProblemJudgeType(machineProblemModel);
        problemJudgeType.setProblemId(problemInfo.getId());
        problemJudgeType.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        problemJudgeTypeMapper.update(problemJudgeType, Wrappers.<ProblemJudgeType>lambdaQuery()
                .eq(ProblemJudgeType::getProblemId, problemInfo.getId()));
        return true;
    }

    @Override
    public IPage<SimpleProblemModel> getSimpleProblemList(PageModel pageParam) {
        Page<Problem> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Problem> problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                .orderByAsc(Problem::getCrtTs));
        List<SimpleProblemModel> list = new ArrayList<>();
        for (Problem p : problemIPage.getRecords()) {
            list.add(ProblemMapstructConvert.INSTANCE.problemToSimpleProblemModel(p));
        }
        return PageUtils.setOpr(problemIPage, new Page<SimpleProblemModel>(), list);
    }

    @Override
    public IPage<PublicSimpleProblemModel> getPublicProblemList(PageModel pageParam) {
        Page<Problem> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Problem> problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode())
                .orderByAsc(Problem::getCrtTs));
        List<PublicSimpleProblemModel> list = new ArrayList<>();
        for (Problem p : problemIPage.getRecords()) {
            PublicSimpleProblemModel problemModel = ProblemMapstructConvert.INSTANCE.problemToPublicSimpleProblemModel(p);
            problemModel.setAcRate(NumberUtils.parsePercent(p.getAcCount(), p.getSubmitCount()));
            list.add(problemModel);
        }
        return PageUtils.setOpr(problemIPage, new Page<PublicSimpleProblemModel>(), list);
    }

    @Override
    public IPage<PublicSimpleProblemModel> getPublicProblemListByCondition(ProblemConditionPageModel problemConditionPageModel) {
        AssertUtils.notNull(problemConditionPageModel.getPageModel(), BaseStatusMsg.APIEnum.PARAM_ERROR, "无分页参数");
        List<Long> problemIds;
        if (problemConditionPageModel.getTagId() != null) {
            problemIds = tagService.getProblemIdListByTagId(problemConditionPageModel.getTagId());
        } else {
            problemIds = problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                    .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode()))
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(problemIds)) {
            return new Page<>();
        }
        List<String> levels = Arrays.asList(problemConditionPageModel.getLevel());
        if (problemConditionPageModel.getLevel() == null) {
            levels = ProblemLevel.getEnumMessageList();
        }
        String titleKey = problemConditionPageModel.getTitleKey();
        if (titleKey == null) {
            titleKey = "";
        }
        Page<Problem> page = new Page<>(problemConditionPageModel.getPageModel().getOffset(), problemConditionPageModel.getPageModel().getLimit());
        IPage<Problem> problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode())
                .in(Problem::getId, problemIds)
                .in(Problem::getLevel, levels)
                .like(Problem::getTitle, "%" + titleKey + "%"));
        List<PublicSimpleProblemModel> list = new ArrayList<>();
        for (Problem p : problemIPage.getRecords()) {
            PublicSimpleProblemModel problemModel = ProblemMapstructConvert.INSTANCE.problemToPublicSimpleProblemModel(p);
            problemModel.setAcRate(NumberUtils.parsePercent(p.getAcCount(), p.getSubmitCount()));
            list.add(problemModel);
        }
        return PageUtils.setOpr(problemIPage, new Page<PublicSimpleProblemModel>(), list);
    }

    // abandoned
    public IPage<PublicSimpleProblemModel> getPublicProblemListByCondition0(ProblemConditionPageModel problemConditionPageModel) {
        AssertUtils.notNull(problemConditionPageModel.getPageModel(), BaseStatusMsg.APIEnum.PARAM_ERROR, "无分页参数");
        List<Long> problemIds;
        if (problemConditionPageModel.getTagId() != null) {
            problemIds = tagService.getProblemIdListByTagId(problemConditionPageModel.getTagId());
        } else {
            problemIds = problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                    .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode()))
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(problemIds)) {
            return new Page<>();
        }
        log.info("afterTagIdFilter problemIds={}", problemIds);
        if (problemConditionPageModel.getLevel() != null) {
            problemIds = problemMapper.selectList(Wrappers.<Problem>lambdaQuery()
                    .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode())
                    .in(Problem::getId, problemIds)
                    .eq(Problem::getLevel, problemConditionPageModel.getLevel()))
                    .stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(problemIds)) {
            return new Page<>();
        }
        log.info("afterLevelFilter problemIds={}", problemIds);
        IPage<Problem> problemIPage = new Page<>();
        Page<Problem> page = new Page<>(problemConditionPageModel.getPageModel().getOffset(), problemConditionPageModel.getPageModel().getLimit());
        if (problemConditionPageModel.getTitleKey() != null) {
            problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                    .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode())
                    .in(Problem::getId, problemIds)
                    .like(Problem::getTitle, "%" + problemConditionPageModel.getTitleKey() + "%"));
        } else {
            problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                    .eq(Problem::getStatus, ProblemStatus.NORMAL_VISIBLE.getCode())
                    .in(Problem::getId, problemIds));
        }
        log.info("afterTitleKeyFilter problemIPage={}", JSONObject.toJSONString(problemIPage));
        List<PublicSimpleProblemModel> list = new ArrayList<>();
        for (Problem p : problemIPage.getRecords()) {
            PublicSimpleProblemModel problemModel = ProblemMapstructConvert.INSTANCE.problemToPublicSimpleProblemModel(p);
            problemModel.setAcRate(NumberUtils.parsePercent(p.getAcCount(), p.getSubmitCount()));
            list.add(problemModel);
        }
        return PageUtils.setOpr(problemIPage, new Page<PublicSimpleProblemModel>(), list);
    }

    @Override
    public DetailProblemModel getDetailProblemInfoById(Long problemId, Boolean visible) {
        Problem problem = problemMapper.selectById(problemId);
        AssertUtils.notNull(problem, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(!problem.getStatus().equals(ProblemStatus.ABNORMAL.getCode()), BaseStatusMsg.ABNORMAL_PROBLEM);
        if (visible) {
            AssertUtils.isTrue(!problem.getStatus().equals(ProblemStatus.NORMAL_INVISIBLE.getCode()),
                    BaseStatusMsg.NORMAL_INVISIBLE_PROBLEM);
        } else {
            AssertUtils.isTrue(!problem.getStatus().equals(ProblemStatus.NORMAL_VISIBLE.getCode()),
                    BaseStatusMsg.NORMAL_VISIBLE_PROBLEM);
        }
        DetailProblemModel detailProblemModel = ProblemMapstructConvert.INSTANCE.problemToDetailProblemModel(problem);
        // 提交统计数据
        Map<String, Integer> statistic = SubmitStatus.getStatisticMap();
        List<Submit> submitList = submitMapper.selectList(Wrappers.<Submit>lambdaQuery()
                .eq(Submit::getProblemId, problemId));
        for (Submit s : submitList) {
            if (!statistic.containsKey(s.getSubmitStatus())) {
                log.error("db submitStatus error={}", s.getSubmitStatus());
                continue;
            }
            statistic.put(s.getSubmitStatus(), statistic.get(s.getSubmitStatus()) + 1);
        }
        detailProblemModel.setStatistic(statistic);
        // 评测方式数据
        ProblemJudgeType problemJudgeType = problemJudgeTypeMapper.selectOne(Wrappers.<ProblemJudgeType>lambdaQuery()
                .eq(ProblemJudgeType::getProblemId, problemId));
        detailProblemModel.setProblemType(problemJudgeType.getProblemType());
        detailProblemModel.setSpj(problemJudgeType.getSpj());
        JudgeType judgeType = judgeTypeMapper.selectById(problemJudgeType.getJudgeTypeId());
        detailProblemModel.setJudgeTypeName(judgeType.getName());
        // 题目标签数据
        detailProblemModel.setTagList(tagService.getProblemTagByProblemId(problemId));
        return detailProblemModel;
    }


    @Override
    public long submitProblem(SubmitModel submitModel, Long userId) {
        Problem problemInfo = problemMapper.selectById(submitModel.getProblemId());
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(ProblemLanguage.isContainMessage(Arrays.asList(submitModel.getLanguage())),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "代码语言不合法或不支持");
        /**<== 修改或添加相关表数据 begin ==>**/
        // 用户表：自增submitCount
        userMapper.updateSubmitCountIncrementOne(userId);
        // 用户解决题目表
        SolvedProblem solvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getProblemId, submitModel.getProblemId()));
        if (solvedProblem == null) {
            // 首次提交该题：插入数据
            SolvedProblem toSolvedProblem = new SolvedProblem();
            toSolvedProblem.setProblemId(submitModel.getProblemId());
            toSolvedProblem.setUserId(userId);
            toSolvedProblem.setSubmitStatus(SubmitStatus.PENDING.getName());
            solvedProblemMapper.insert(toSolvedProblem);
            // 题目信息表更新：自增submit_num
            problemMapper.updateSubmitNumIncrementOne(submitModel.getProblemId());
        } else if (!solvedProblem.getSubmitStatus().equals(SubmitStatus.ACCEPTED.getName())) {
            // 非accepted状态需要：更新最新状态
            solvedProblem.setSubmitStatus(SubmitStatus.PENDING.getName());
            solvedProblem.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
            solvedProblemMapper.updateById(solvedProblem);
        }
        // 题目信息表：自增submit_count
        problemMapper.updateSubmitCountIncrementOne(submitModel.getProblemId());
        // 提交评测表：插入数据
        Submit submit = SubmitMapstructConvert.INSTANCE.submitModelToSubmit(submitModel);
        submit.setSubmitStatus(SubmitStatus.PENDING.getName());
        submit.setSourceId(problemInfo.getSourceId());
        submit.setCreatorId(userId);
        submit.setCreator(userService.getUserInfoById(userId).getUsername());
        submitMapper.insert(submit);
        /**<== 修改或添加相关表数据 end ==>**/

        /**<== 封装kafka消息格式，并发送消息评测 begin ==>**/

        // mock评测数据（针对非比赛问题）
        if (problemInfo.getSourceId() == 0) {
            boolean ac = false;
            if ((int) (Math.random() * 2) == 1) {
                ac = true;
            }
            SubmitResultModel submitResultModel = new SubmitResultModel();
            submitResultModel.setSubmitId(submit.getId());
            submitResultModel.setSubmitStatus(ac ? SubmitStatus.ACCEPTED.getName() : SubmitStatus.WRONG_ANSWER.getName());
            submitResultModel.setMemory("2000kb");
            submitResultModel.setTime("2000ms");
            refreshSubmitProblem(submitResultModel, userId);
        }

        return submit.getId();
        /**<== 封装kafka消息格式，并发送消息评测 end ==>**/
    }

    public void refreshSubmitProblem(SubmitResultModel submitResult, Long userId) {
        // 校验参数：
        Problem problemInfo = problemMapper.selectById(submitResult.getProblemId());
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(SubmitStatus.isContain(submitResult.getSubmitStatus()), BaseStatusMsg.APIEnum.PARAM_ERROR, "评测结果不合法");
        // 用户表
        SolvedProblem solvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getProblemId, submitResult.getProblemId())
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName()));
        if (solvedProblem == null && SubmitStatus.ACCEPTED.getName().equals(submitResult.getSubmitStatus())) {
            userMapper.updateAcNumIncrementOne(userId);
        }
        // 用户解决问题表
        if (solvedProblem == null) {
            SolvedProblem newSolvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                            .eq(SolvedProblem::getUserId, userId)
                            .eq(SolvedProblem::getProblemId, submitResult.getProblemId()));
            newSolvedProblem.setSubmitStatus(submitResult.getSubmitStatus());
            newSolvedProblem.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
            solvedProblemMapper.updateById(newSolvedProblem);
        }
        // 题目信息表
        if (SubmitStatus.ACCEPTED.getName().equals(submitResult.getSubmitStatus())) {
            problemMapper.updateAcCountIncrementOne(submitResult.getProblemId());
            if (solvedProblem == null) {
                problemMapper.updateAcNumIncrementOne(submitResult.getProblemId());
            }
        }
        // 提交评测表
        Submit submit = submitMapper.selectById(submitResult.getSubmitId());
        AssertUtils.notNull(submit, BaseStatusMsg.APIEnum.PARAM_ERROR, "提交评测信息不存在");
        submit.setTime(submitResult.getTime());
        submit.setMemory(submitResult.getMemory());
        submit.setSubmitStatus(submitResult.getSubmitStatus());
        submitMapper.updateById(submit);
    }

}

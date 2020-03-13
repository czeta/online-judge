package com.czeta.onlinejudge.service.impl;

import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.consts.FileConstant;
import com.czeta.onlinejudge.convert.ProblemMapstructConvert;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.ProblemJudgeType;
import com.czeta.onlinejudge.dao.entity.ProblemTag;
import com.czeta.onlinejudge.dao.mapper.ProblemJudgeTypeMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemMapper;
import com.czeta.onlinejudge.dao.mapper.ProblemTagMapper;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.ProblemType;
import com.czeta.onlinejudge.model.param.MachineProblemModel;
import com.czeta.onlinejudge.model.param.SpiderProblemModel;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.FileNameMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Override
    public long saveNewProblemBySpider(SpiderProblemModel spiderProblemModel, Long adminId) {
        // 调用爬虫服务
        return 1;
    }

    @Override
    public long saveNewProblemByMachine(MachineProblemModel machineProblemModel, Long adminId) {
        AssertUtils.notNull(adminId, BaseStatusMsg.APIEnum.PARAM_ERROR);
        // 题目信息
        Problem problemInfo = ProblemMapstructConvert.INSTANCE.machineProblemToProblem(machineProblemModel);
        problemInfo.setCreator(adminService.getAdminInfoById(adminId).getUsername());
        try {
            problemMapper.insert(problemInfo);
        } catch (Exception e) {
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
    public boolean uploadProblemJudgeFile(MultipartFile[] files, Long problemId) throws Exception {
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
            int no = judgeDataDir.listFiles().length / 2 + 1;
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
    public boolean uploadOtherProblemJudgeFile(MultipartFile file, Long problemId, ProblemType problemType) throws Exception {
        // 校验：题目ID是否存在
        Problem problemInfo = problemMapper.selectById(problemId);
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目尚未创建");
        // 校验：文件名是否复合
        String fileName = file.getOriginalFilename();
        if (problemType.equalByCode(ProblemType.FUNCTION)) {
            AssertUtils.isTrue("insert.cpp".equals(fileName), BaseStatusMsg.APIEnum.PARAM_ERROR, "文件名不正确");
        } else if (problemType.equalByCode(ProblemType.SPJ)) {
            AssertUtils.isTrue("spj.cpp".equals(fileName), BaseStatusMsg.APIEnum.PARAM_ERROR,"文件名不正确");
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
}

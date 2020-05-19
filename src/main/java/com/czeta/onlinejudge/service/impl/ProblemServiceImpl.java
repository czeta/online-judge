package com.czeta.onlinejudge.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czeta.onlinejudge.annotation.SpiderNameAnnotationHandler;
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
import com.czeta.onlinejudge.model.result.SpiderProblemResultModel;
import com.czeta.onlinejudge.mq.SubmitMessage;
import com.czeta.onlinejudge.mq.producer.SubmitProducer;
import com.czeta.onlinejudge.service.AdminService;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.service.TagService;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.spider.SpiderService;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.spider.request.SpiderRequestBody;
import com.czeta.onlinejudge.utils.spider.response.SpiderResponse;
import com.czeta.onlinejudge.utils.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @Autowired
    private SubmitProducer submitProducer;

    @Autowired
    private SpiderNameAnnotationHandler spiderHandler;

    @Override
    public long saveNewProblemBySpider(SpiderProblemModel spiderProblemModel, Long adminId) {
        // 评测校验
        JudgeType judgeTypeInfo = judgeTypeMapper.selectById(spiderProblemModel.getJudgeTypeId());
        AssertUtils.notNull(judgeTypeInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "评测类型不存在");
        AssertUtils.isTrue(judgeTypeInfo.getType().equals(JudgeTypeEnum.JUDGE_SPIDER.getCode()), BaseStatusMsg.APIEnum.PARAM_ERROR, "该评测类型不是爬虫评测");
        AssertUtils.isTrue(judgeTypeInfo.getStatus().equals(JudgeServerStatus.NORMAL.getCode()), BaseStatusMsg.APIEnum.PARAM_ERROR, "该爬虫服务异常或暂时不可用");
        SpiderService spiderService = spiderHandler.getSpiderServiceMap().get(judgeTypeInfo.getName());
        // 爬取题目
        SpiderProblemResultModel resultModel = (SpiderProblemResultModel) spiderService.execute(String.valueOf(spiderProblemModel.getSpiderProblemId()));
        // 持久化
        // 题目信息
        Problem problemInfo = ProblemMapstructConvert.INSTANCE.spiderProblemResultModelToProblem(resultModel);
        problemInfo.setTitle(spiderProblemModel.getTitle() != null ? spiderProblemModel.getTitle() : problemInfo.getTitle());
        problemInfo.setSourceId(spiderProblemModel.getSourceId());
        problemInfo.setSourceName(spiderProblemModel.getSourceName());
        problemInfo.setIoMode("Standard IO");
        problemInfo.setLevel(spiderProblemModel.getLevel());
        problemInfo.setStatus(spiderProblemModel.getStatus());
        problemInfo.setCreator(adminService.getAdminInfoById(adminId).getUsername());
        try {
            problemMapper.insert(problemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_NAME);
        }
        // 题目标签
        Long problemId = problemInfo.getId();
        ProblemTag problemTag = new ProblemTag();
        problemTag.setProblemId(problemId);
        for (Integer tagId : spiderProblemModel.getTagId()) {
            problemTag.setTagId(tagId);
            problemTagMapper.insert(problemTag);
            problemTag.setId(null);
        }
        // 题目评测方式
        ProblemJudgeType problemJudgeType = new ProblemJudgeType();
        problemJudgeType.setProblemId(problemId);
        problemJudgeType.setJudgeTypeId(spiderProblemModel.getJudgeTypeId());
        problemJudgeType.setSpiderProblemId(spiderProblemModel.getSpiderProblemId());
        try {
            problemJudgeTypeMapper.insert(problemJudgeType);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM_JUDGE_TYPE);
        }
        return problemId;
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
        problemJudgeType.setSpjVersion(DigestUtils.sha256Hex(machineProblemModel.getSpjLanguage() + machineProblemModel.getSpjCode()));
        problemJudgeType.setProblemId(problemId);
        try {
            problemJudgeTypeMapper.insert(problemJudgeType);
        } catch (Exception e) {
            throw new APIRuntimeException(BaseStatusMsg.EXISTED_PROBLEM_JUDGE_TYPE);
        }
        return problemId;
    }

    @Override
    public boolean uploadTestCaseZip(MultipartFile file, Boolean spj, Long problemId, Long adminId) throws Exception {
        String uploadTestCaseDir = multipartProperties.getUploadTestCase() + problemId + "/";
        // (1)删除旧文件夹
        FileUtils.deleteDirectory(new File(uploadTestCaseDir));
        // (2)上传zip
        UploadUtils.upload(uploadTestCaseDir, file,
                originalFilename -> file.getOriginalFilename(), Arrays.asList(FileConstant.SUFFIX_EXTENSION_ZIP));
        // (3)解压缩zip，并将文件\r\n转化为\n再写入；校验文件名规则
        Map<String, JSONObject> entryFilesMap = new HashMap<>();
        List<String> entryFilesNameList = new ArrayList<>();
        File zipFile = new File(uploadTestCaseDir + file.getOriginalFilename());
        ZipArchiveInputStream zais = null;
        try {
            zais = new ZipArchiveInputStream(new FileInputStream(zipFile));
            ArchiveEntry entryFile = null;
            while ((entryFile = zais.getNextEntry()) != null) {
                // 压缩包中的一个文件名
                String entryFileName = entryFile.getName();
                entryFilesNameList.add(entryFileName);
                byte[] content = new byte[(int) entryFile.getSize()];
                zais.read(content);
                JSONObject fileInfo = new JSONObject();
                // 转换为unix文件格式：\\n结尾
                String contentStr = new String(content).replaceAll("\\r\\n", "\\n");
                fileInfo.put("content", contentStr);
                fileInfo.put("size", contentStr.length());
                if (FilenameUtils.getExtension(entryFileName).equals(FileConstant.SUFFIX_EXTENSION_OUT)) {
                    fileInfo.put("stripped_output_md5", DigestUtils.md5Hex(contentStr.trim()));
                }
                entryFilesMap.put(entryFileName, fileInfo);
            }
            // !spj: 校验文件名是否成对出现，从1开始，并且后缀为in或out; spj: 文件名单个出现，并且文件名从1开始递增，后缀是in
            boolean result = checkFileName(entryFilesNameList, spj);
            AssertUtils.isTrue(result, BaseStatusMsg.APIEnum.FAILED,
                    "文件校验失败：如果是!spj：文件名需要成对出现，并且文件名一致（从1开始递增），后缀分别是in和out；如果是spj：文件名单个出现，并且文件名从1开始递增，后缀是in");
            for (Map.Entry<String, JSONObject> item : entryFilesMap.entrySet()) {
                OutputStream os = null;
                try {
                    //把解压出来的文件写到指定路径
                    String fPath = uploadTestCaseDir + item.getKey();
                    File f = new File(fPath);
                    os = new BufferedOutputStream(new FileOutputStream(f));
                    String content = (String) (item.getValue().get("content"));
                    os.write(content.getBytes("utf-8"));
                } catch (IOException e) {
                    log.error("ProblemServiceImpl uploadTestCaseZip decompress Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
                    throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, "上传文件失败");
                } finally {
                    if(os != null) {
                        os.flush();
                        os.close();
                    }
                }
            }
            // (4)生成评测文件元数据info文件：长度，output去除结尾空格后的md5等等test_case的元数据
            JSONObject infoJson = new JSONObject();
            infoJson.put("spj", spj);
            JSONObject infoTestCaseJson = new JSONObject();
            if (spj) {
                for (int i = 1; i <= entryFilesNameList.size(); ++i) {
                    JSONObject infoTestCaseItemJson = new JSONObject();
                    infoTestCaseItemJson.put("input_name", i + "." + FileConstant.SUFFIX_EXTENSION_IN);
                    infoTestCaseItemJson.put("input_size", entryFilesMap.get(i + "." + FileConstant.SUFFIX_EXTENSION_IN).get("size"));
                    infoTestCaseJson.put(String.valueOf(i), infoTestCaseItemJson);
                }
            } else {
                for (int i = 1; i <= entryFilesNameList.size() / 2; ++i) {
                    JSONObject infoTestCaseItemJson = new JSONObject();
                    infoTestCaseItemJson.put("input_name", i + "." + FileConstant.SUFFIX_EXTENSION_IN);
                    infoTestCaseItemJson.put("output_name", i + "." + FileConstant.SUFFIX_EXTENSION_OUT);
                    infoTestCaseItemJson.put("input_size", entryFilesMap.get(i + "." + FileConstant.SUFFIX_EXTENSION_IN).get("size"));
                    infoTestCaseItemJson.put("output_size", entryFilesMap.get(i + "." + FileConstant.SUFFIX_EXTENSION_OUT).get("size"));
                    infoTestCaseItemJson.put("stripped_output_md5", entryFilesMap.get(i + "." + FileConstant.SUFFIX_EXTENSION_OUT).get("stripped_output_md5"));
                    infoTestCaseJson.put(String.valueOf(i), infoTestCaseItemJson);
                }
            }
            infoJson.put("test_cases", infoTestCaseJson);
            // (5)将评测文件元数据info文件存入
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(new File(uploadTestCaseDir + "info")));
                os.write(infoJson.toString().getBytes("utf-8"));
            } catch (IOException e) {
                log.error("ProblemServiceImpl uploadTestCaseZip writeInfo Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
                throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, "上传文件失败");
            } finally {
                if(os != null) {
                    os.flush();
                    os.close();
                }
            }
        } catch (IOException ex) {
            log.error("ProblemServiceImpl uploadTestCaseZip Exception={} StackTrace={}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, "上传文件失败");
        } finally {
            try {
                if(zais != null) {
                    zais.close();
                }
            } catch (IOException e) {
                log.error("ProblemServiceImpl uploadTestCaseZip closeZais Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
                throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, "上传文件失败");
            }
        }
        return true;
    }

    private boolean checkFileName(List<String> fileNameList, Boolean spj) {
        if (!spj && fileNameList.size() % 2 != 0) {
            return false;
        }
        if (spj) {
            int maxPrefix = fileNameList.size();
            for (int i = 1; i <= maxPrefix; ++i) {
                if (!fileNameList.contains(String.valueOf(i) + "." + FileConstant.SUFFIX_EXTENSION_IN)) {
                    return false;
                }
            }
        } else {
            int maxPrefix = fileNameList.size() / 2;
            for (int i = 1; i <= maxPrefix; ++i) {
                if (!fileNameList.contains(String.valueOf(i) + "." + FileConstant.SUFFIX_EXTENSION_IN)
                        || !fileNameList.contains(String.valueOf(i) + "." + FileConstant.SUFFIX_EXTENSION_OUT)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void downloadTestCaseZip(Long problemId, HttpServletResponse response) throws Exception {
        String downloadDir = multipartProperties.getUploadTestCase() + problemId;
        boolean flag = false;
        File targetDir = new File(downloadDir);
        for (File targetFile : targetDir.listFiles()) {
            if (FilenameUtils.getExtension(targetFile.getName()).equals(FileConstant.SUFFIX_EXTENSION_ZIP)) {
                DownloadUtils.download(downloadDir, targetFile.getName(), Arrays.asList(FileConstant.SUFFIX_EXTENSION_ZIP), response, (dir, fileName0, file, fileExtension, contentType, length) -> {
                    return true;
                });
                flag = true;
                break;
            }
        }
        AssertUtils.isTrue(flag, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目所在文件夹不存在或该文件不存在");
    }

    @Override
    public List<String> getTestCaseFileList(Long problemId) {
        File testCaseDir = new File(multipartProperties.getUploadTestCase() + problemId);
        AssertUtils.isTrue(testCaseDir.exists(), BaseStatusMsg.APIEnum.PARAM_ERROR, "题目所在文件夹不存在");
        List<String> fileNameList = new ArrayList<>();
        for (File f : testCaseDir.listFiles()) {
            if (FilenameUtils.getExtension(f.getName()).equals(FileConstant.SUFFIX_EXTENSION_IN)
                    || FilenameUtils.getExtension(f.getName()).equals(FileConstant.SUFFIX_EXTENSION_OUT)) {
                fileNameList.add(f.getName());
            }
        }
        Collections.sort(fileNameList);
        return fileNameList;
    }

    @Override
    public Boolean compileSpjCode(String spjCode, String spjLanguage, String judgeServerName) {
        final String LANGUAGE_JSON_STR = "[{\"spj\":{\"config\":{\"command\":\"{exe_path} {in_file_path} {user_out_file_path}\",\"exe_name\":\"spj-{spj_version}\",\"seccomp_rule\":\"c_cpp\"},\"compile\":{\"exe_name\":\"spj-{spj_version}\",\"src_name\":\"spj-{spj_version}.c\",\"max_memory\":1073741824,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}\"}},\"name\":\"C\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"{exe_path}\",\"seccomp_rule\":{\"File IO\":\"c_cpp_file_io\",\"Standard IO\":\"c_cpp\"}},\"compile\":{\"exe_name\":\"main\",\"src_name\":\"main.c\",\"max_memory\":268435456,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}\"}},\"description\":\"GCC 5.4\",\"content_type\":\"text/x-csrc\"},{\"spj\":{\"config\":{\"command\":\"{exe_path} {in_file_path} {user_out_file_path}\",\"exe_name\":\"spj-{spj_version}\",\"seccomp_rule\":\"c_cpp\"},\"compile\":{\"exe_name\":\"spj-{spj_version}\",\"src_name\":\"spj-{spj_version}.cpp\",\"max_memory\":1073741824,\"max_cpu_time\":10000,\"max_real_time\":20000,\"compile_command\":\"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}\"}},\"name\":\"C++\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"{exe_path}\",\"seccomp_rule\":{\"File IO\":\"c_cpp_file_io\",\"Standard IO\":\"c_cpp\"}},\"compile\":{\"exe_name\":\"main\",\"src_name\":\"main.cpp\",\"max_memory\":1073741824,\"max_cpu_time\":10000,\"max_real_time\":20000,\"compile_command\":\"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}\"}},\"description\":\"G++ 5.4\",\"content_type\":\"text/x-c++src\"},{\"name\":\"Java\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main\",\"seccomp_rule\":null,\"memory_limit_check_only\":1},\"compile\":{\"exe_name\":\"Main\",\"src_name\":\"Main.java\",\"max_memory\":-1,\"max_cpu_time\":5000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8\"}},\"description\":\"OpenJDK 1.8\",\"content_type\":\"text/x-java\"},{\"name\":\"Python2\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/python {exe_path}\",\"seccomp_rule\":\"general\"},\"compile\":{\"exe_name\":\"solution.pyc\",\"src_name\":\"solution.py\",\"max_memory\":134217728,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/python -m py_compile {src_path}\"}},\"description\":\"Python 2.7\",\"content_type\":\"text/x-python\"},{\"name\":\"Python3\",\"config\":{\"run\":{\"env\":[\"LANG=en_US.UTF-8\",\"LANGUAGE=en_US:en\",\"LC_ALL=en_US.UTF-8\"],\"command\":\"/usr/bin/python3 {exe_path}\",\"seccomp_rule\":\"general\"},\"compile\":{\"exe_name\":\"__pycache__/solution.cpython-35.pyc\",\"src_name\":\"solution.py\",\"max_memory\":134217728,\"max_cpu_time\":3000,\"max_real_time\":10000,\"compile_command\":\"/usr/bin/python3 -m py_compile {src_path}\"}},\"description\":\"Python 3.5\",\"content_type\":\"text/x-python\"}]";
        final Map<String, JSONObject> LANGUAGE_JSON_MAP = new HashMap<>();
        JSONArray jsonArray = JSONArray.parseArray(LANGUAGE_JSON_STR);;
        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject obj = jsonArray.getJSONObject(i);
            LANGUAGE_JSON_MAP.put((String) obj.get("name"), obj);
        }
        spjCode = Base64.decodeToString(spjCode);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("src", spjCode);
            jsonObject.put("spj_version", DigestUtils.sha256Hex(spjLanguage + spjCode));
            jsonObject.put("spj_compile_config", LANGUAGE_JSON_MAP.get(spjLanguage).getJSONObject("spj").getJSONObject("compile"));
            SpiderRequestBody spiderRequestBody = SpiderRequestBody.json(JSONObject.toJSONString(jsonObject), "utf-8");
            ByteArrayEntity entity =  new ByteArrayEntity(spiderRequestBody.getBody(), spiderRequestBody.getContentType());
            List<JudgeType> judgeTypeList = judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                    .eq(JudgeType::getStatus, JudgeServerStatus.NORMAL.getCode())
                    .eq(JudgeType::getName, judgeServerName));
            AssertUtils.notEmpty(judgeTypeList, BaseStatusMsg.APIEnum.FAILED, "请选择可用的评测机");
            JudgeType judgeType = judgeTypeList.get(0);
            HttpPost httpPost = new HttpPost(judgeType.getUrl() + "/compile_spj");
            httpPost.setEntity(entity);
            httpPost.addHeader("X-Judge-Server-Token", judgeType.getVisitToken());
            response = httpClient.execute(httpPost);
            SpiderResponse spiderResponse = SpiderResponse.build(null, response);
            log.info("ProblemServiceImpl compileSpjCode spiderResponseJson={}", JSONObject.toJSONString(spiderResponse.getJsonObject()));
            if (spiderResponse.getJsonObject().get("data").toString().equals("success"))
                return true;
        } catch (IOException e) {
            throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, e.getMessage());
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                throw new APIRuntimeException(BaseStatusMsg.APIEnum.FAILED, e.getMessage());
            }
        }
        return false;
    }

    @Override
    public MachineProblemModel getProblemInfo(Long problemId) {
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
        problemJudgeType.setSpjVersion(DigestUtils.sha256Hex(machineProblemModel.getSpjLanguage() + machineProblemModel.getSpjCode()));
        problemJudgeType.setProblemId(problemInfo.getId());
        problemJudgeType.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        problemJudgeTypeMapper.update(problemJudgeType, Wrappers.<ProblemJudgeType>lambdaQuery()
                .eq(ProblemJudgeType::getProblemId, problemInfo.getId()));
        return true;
    }

    @Override
    public boolean updateProblemStatus(Short status, Long problemId, Long adminId) {
        Problem problemInfo = new Problem();
        problemInfo.setStatus(status);
        problemInfo.setId(problemId);
        problemMapper.updateById(problemInfo);
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
    public IPage<SimpleProblemModel> getSimpleProblemListByTitleKey(PageModel<String> pageParam) {
        Page<Problem> page = new Page<>(pageParam.getOffset(), pageParam.getLimit());
        IPage<Problem> problemIPage = problemMapper.selectPage(page, Wrappers.<Problem>lambdaQuery()
                .like(Problem::getTitle, pageParam.getParamData())
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
        detailProblemModel.setCodeTemplate(problemJudgeType.getCodeTemplate());
        detailProblemModel.setSpj(problemJudgeType.getSpj());
        JudgeType judgeType = judgeTypeMapper.selectById(problemJudgeType.getJudgeTypeId());
        detailProblemModel.setJudgeTypeName(judgeType.getName());
        // 题目标签数据
        detailProblemModel.setTagList(tagService.getProblemTagByProblemId(problemId));
        return detailProblemModel;
    }


    @Override
    public long submitProblem(SubmitModel submitModel, Long userId) {
        // 校验参数：
        Problem problemInfo = problemMapper.selectById(submitModel.getProblemId());
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(ProblemLanguage.isContainMessage(Arrays.asList(submitModel.getLanguage())),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "代码语言不合法或不支持");
        AssertUtils.isTrue(Arrays.asList(problemInfo.getLanguage().split(",")).contains(submitModel.getLanguage()),
                BaseStatusMsg.APIEnum.PARAM_ERROR, "代码语言不合法或不支持");
        // 用户表
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
        // 题目信息表
        problemMapper.updateSubmitCountIncrementOne(submitModel.getProblemId());
        // 提交评测表
        Submit submit = SubmitMapstructConvert.INSTANCE.submitModelToSubmit(submitModel);
        submit.setSubmitStatus(SubmitStatus.PENDING.getName());
        submit.setSourceId(problemInfo.getSourceId());
        submit.setCreatorId(userId);
        submit.setCreator(userService.getUserInfoById(userId).getUsername());
        submitMapper.insert(submit);

        // 封装kafka消息格式，并发送消息评测
        sendMessage(userId, submit.getId(), submitModel, problemInfo);

        return submit.getId();
    }

    private void sendMessage(Long userId, Long submitId, SubmitModel submitModel, Problem problemInfo) {
        SubmitMessage submitMessage = new SubmitMessage();
        submitMessage.setSubmitId(submitId);

        submitMessage.setUserId(userId);
        submitMessage.setCode(submitModel.getCode());
        submitMessage.setLanguage(submitModel.getLanguage());

        submitMessage.setProblemId(submitModel.getProblemId());
        submitMessage.setSourceId(problemInfo.getSourceId());
        submitMessage.setTimeLimit(problemInfo.getTimeLimit());
        submitMessage.setMemoryLimit(problemInfo.getMemoryLimit());

        ProblemJudgeType problemJudgeType = problemJudgeTypeMapper.selectOne(Wrappers.<ProblemJudgeType>lambdaQuery()
                .eq(ProblemJudgeType::getProblemId, problemInfo.getId()));
        JudgeType judgeType = judgeTypeMapper.selectById(problemJudgeType.getJudgeTypeId());
        submitMessage.setJudgeType(judgeType.getType());
        submitMessage.setJudgeName(judgeType.getName());
        submitMessage.setJudgeUrl(judgeType.getUrl());
        List<JudgeType> judgeMachineList = judgeTypeMapper.selectList(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getType, JudgeTypeEnum.JUDGE_MACHINE.getCode())
                .eq(JudgeType::getStatus, JudgeServerStatus.NORMAL.getCode())
                .eq(JudgeType::getName, judgeType.getName()));
        submitMessage.setJudgeTypeList(judgeMachineList);
        submitMessage.setVisitToken(judgeType.getVisitToken());
        submitMessage.setSpj(problemJudgeType.getSpj());
        submitMessage.setSpjCode(problemJudgeType.getSpjCode());
        submitMessage.setSpjLanguage(problemJudgeType.getSpjLanguage());
        submitMessage.setSpjVersion(problemJudgeType.getSpjVersion());
        submitMessage.setSpiderProblemId(problemJudgeType.getSpiderProblemId());

        submitProducer.send(submitMessage);
    }
}

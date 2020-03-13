package com.czeta.onlinejudge.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.consts.FileConstant;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.enums.ProblemType;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.service.*;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.czeta.onlinejudge.utils.utils.DownloadUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName AdminController
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/3 9:11
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Admin Manager Controller")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private JudgeService judgeService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private MultipartProperties multipartProperties;

    @ApiOperation(value = "分页获得所有用户的详情信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/userInfoList")
    public APIResult<IPage<User>> getUserInfoList(@RequestBody PageModel pageModel) {
        return new APIResult<>(userService.getUserInfoList(pageModel));
    }

    @ApiOperation(value = "根据用户名关键字分页获取相关用户信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData为搜索用户名关键字", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/searchResult")
    public APIResult<IPage<User>> getUserSearchResult(@RequestBody PageModel<String> pageModel) {
        return new APIResult<>(userService.getUserInfosByUsernameKey(pageModel));
    }

    @ApiOperation(value = "添加新的用户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRegisterModel", value = "新的用户注册信息", dataType = "UserRegisterModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/save")
    public APIResult saveNewUser(@RequestBody UserRegisterModel userRegisterModel) {
        userService.saveNewUser(userRegisterModel);
        return new APIResult();
    }

    @ApiOperation(value = "重置用户密码为与用户名一致", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "重置密码的目标用户名", dataType = "String", paramType = "path", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/password/update/{username}")
    public APIResult<Boolean> updateUserPassword(@PathVariable String username) {
        return new APIResult<>(userService.resetUserPasswordByUsername(username));
    }


    @ApiOperation(value = "禁用用户账户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "禁用账户的目标用户名", dataType = "String", paramType = "path", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/account/update/{username}")
    public APIResult<Boolean> updateUserAccount(@PathVariable String username) {
        return new APIResult<>(userService.disableUserAccountByUsername(username));
    }

    @ApiOperation(value = "按条件批量注册用户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rangedUserModel", value = "注册条件", dataType = "RangedUserModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "失败"),
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/generateUsers")
    public void generateUsers(@RequestBody RangedUserModel rangedUserModel, HttpServletResponse response) throws Exception {
        List<UserRegisterModel> userRegisterModels = userService.insertRangedUserList(rangedUserModel);
        File saveDir = new File(multipartProperties.getUploadPath());
        if (!saveDir.exists()) {
            boolean flag = saveDir.mkdirs();
            if (!flag) {
                log.error("上传文件中创建{}目录失败", saveDir);
                throw new APIRuntimeException(IBaseStatusMsg.APIEnum.FAILED);
            }
        }
        String fileName = FileConstant.PREFIX_GENERATE_USERS
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS")) + FileConstant.SUFFIX_EXCEL;
        String filePath =  multipartProperties.getUploadPath() + fileName;
        EasyExcel.write(filePath, UserRegisterModel.class).sheet("批量生成用户").doWrite(userRegisterModels);
        DownloadUtils.download(multipartProperties.getUploadPath(), fileName, multipartProperties.getAllowDownloadFileExtensions(), response, (dir, fileName0, file, fileExtension, contentType, length) -> {
            return true;
        });
    }


    @ApiOperation(value = "获得用户申请的实名认证信息列表（待审核）", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/userManager/appliedCertificationList")
    public APIResult<IPage<AppliedCertificationModel>> getAppliedCertificationList(@RequestBody PageModel pageModel) {
        return new APIResult<>(certificationService.getAppliedCertificationList(pageModel));
    }

    @ApiOperation(value = "审核实名认证信息：通过或不通过", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "审核结果：1表示通过、-1表示不通过", dataType = "Short", paramType = "query", required = true),
            @ApiImplicitParam(name = "userId", value = "申请的用户ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/appliedCertification/update")
    public APIResult<Boolean> updateAppliedCertification(@RequestParam Short status, @RequestParam Long userId) {
        return new APIResult<>(certificationService.updateAppliedCertificationByUserId(status, userId));
    }


    @ApiOperation(value = "获得实名认证类型列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/certManager/certTypeList")
    public APIResult<List<Certification>> getCertificationTypeList() {
        return new APIResult<>(certificationService.getCertificationTypes());
    }


    @ApiOperation(value = "添加新的实名认证类型", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "实名认证类型", dataType = "String", paramType = "query", required = true)})
    @ApiResponses({
            @ApiResponse(code = 2101, message = "已存在的名称")
    })
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/save")
    public APIResult saveNewCertificationType(@RequestParam String type) {
        certificationService.saveNewCertificationType(type);
        return new APIResult();
    }


    @ApiOperation(value = "更新实名认证类型的状态：启用或弃用", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certificationModel", value = "实名认证类型", dataType = "CertificationModel", paramType = "body", required = true)})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/update")
    public APIResult<Boolean> updateCertification(@RequestBody CertificationModel certificationModel) {
        return new APIResult<>(certificationService.updateCertification(certificationModel));
    }

    @ApiOperation(value = "分页获取普通管理员详细信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/adminManager/adminInfoList")
    public APIResult<IPage<Admin>> getAdminInfoList(@RequestBody PageModel pageModel) {
        return new APIResult<>(adminService.getAdminInfoList(pageModel));
    }


    @ApiOperation(value = "根据管理员用户名关键字分页获得相关普通管理员账号信息", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData为用户名关键字", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/adminManager/searchResult")
    public APIResult<IPage<Admin>> getAdminSearchResult(@RequestBody PageModel<String> pageModel) {
        return new APIResult<>(adminService.getAdminInfoListByUsernameKey(pageModel));
    }

    @ApiOperation(value = "添加新的普通管理员账户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminRegisterModel", value = "管理员注册基本信息", dataType = "AdminRegisterModel", paramType = "body", required = true)})
    @ApiResponses({
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/save")
    public APIResult saveNewAdmin(@RequestBody AdminRegisterModel adminRegisterModel) {
        adminService.saveNewAdmin(adminRegisterModel);
        return new APIResult();
    }


    @ApiOperation(value = "重置普通管理员密码", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "重置密码的目标管理员用户名", dataType = "String", paramType = "path", required = true)})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/password/update/{username}")
    public APIResult<Boolean> updateAdminPassword(@PathVariable String username) {
        return new APIResult<>(adminService.resetAdminPasswordByUsername(username));
    }


    @ApiOperation(value = "禁用普通管理员账号", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "禁用账户的目标管理员用户名", dataType = "String", paramType = "path", required = true)})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/account/update/{username}")
    public APIResult<Boolean> updateAdminAccount(@PathVariable String username) {
        return new APIResult<>(adminService.disableAdminAccountByUsername(username));
    }

    @ApiOperation(value = "分页获得主页公告信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/ancManager/ancInfoList")
    public APIResult<IPage<Announcement>> getHomePageAnnouncementList(@RequestBody PageModel pageModel) {
        return new APIResult<>(announcementService.getHomePageAnnouncementList(pageModel));
    }


    @ApiOperation(value = "根据公告ID获取首页公告信息", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告ID", dataType = "Long", paramType = "path", required = true)})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/ancManager/ancInfo/{id}")
    public APIResult<Announcement> getHomePageAnnouncement(@PathVariable Long id) {
        return new APIResult<>(announcementService.getHomePageAnnouncementById(id));
    }

    @ApiOperation(value = "添加新的首页公告", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "添加的公告实体model", dataType = "AnnouncementModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "userId", value = "管理员id，不过这是解析token得出的，故不需要传入此参数", dataType = "Long", paramType = "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/save")
    public APIResult saveNewHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel, @RequestAttribute Long userId) {
        announcementService.saveNewHomePageAnnouncement(announcementModel, userId);
        return new APIResult();
    }

    @ApiOperation(value = "更新首页公告", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "修改的公告实体model", dataType = "AnnouncementModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/update")
    public APIResult<Boolean> updateHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel) {
        return new APIResult<>(announcementService.updateHomePageAnnouncement(announcementModel));
    }

    @ApiOperation(value = "获取FAQ内容", notes = "需要token：超级admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/faq")
    public APIResult<String> getFAQContent() {
        return new APIResult(announcementService.getFAQContent());
    }

    @ApiOperation(value = "更新FAQ内容", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "修改后的FAQ内容", dataType = "String", paramType = "query", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/faq/update")
    public APIResult<Boolean> updateFAQContent(String content) {
        return new APIResult<>(announcementService.updateFAQContent(content));
    }

    @ApiOperation(value = "获得评测机信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/judgeManager/judgeMachineList")
    public APIResult<List<JudgeType>> getJudgeMachineList() {
        return new APIResult<>(judgeService.getJudgeMachineList());
    }


    @ApiOperation(value = "添加新的评测机", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "judgeTypeModel", value = "评测类型信息model", dataType = "JudgeTypeModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeMachine/save")
    public APIResult saveNewJudgeMachine(@RequestBody JudgeTypeModel judgeTypeModel) {
        judgeService.saveNewJudgeMachine(judgeTypeModel);
        return new APIResult();
    }

    @ApiOperation(value = "获得爬虫信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/judgeManager/judgeSpiderList")
    public APIResult<List<JudgeType>> getJudgeSpiderList() {
        return new APIResult<>(judgeService.getJudgeSpiderList());
    }

    @ApiOperation(value = "添加新的评测爬虫", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "judgeTypeModel", value = "评测类型信息model", dataType = "JudgeTypeModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeSpider/save")
    public APIResult saveNewJudgeSpider(@RequestBody JudgeTypeModel judgeTypeModel) {
        judgeService.saveNewJudgeSpider(judgeTypeModel);
        return new APIResult();
    }

    @ApiOperation(value = "修改评测类型（爬虫or评测机）", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "judgeTypeModel", value = "评测类型信息model", dataType = "JudgeTypeModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeInfo/update")
    public APIResult<Boolean> updateJudgeInfo(@RequestBody JudgeTypeModel judgeTypeModel) {
        return new APIResult<>(judgeService.updateJudgeInfoById(judgeTypeModel));
    }

    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/saveSpider")
    public APIResult<Long> saveNewProblemBySpider(@RequestBody SpiderProblemModel spiderProblemModel, @RequestAttribute Long adminId) throws Exception {
        return new APIResult<>(problemService.saveNewProblemBySpider(spiderProblemModel, adminId));
    }

    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/saveMachine")
    public APIResult<Long> saveNewProblemByMachine(@RequestBody MachineProblemModel machineProblemModel, @RequestAttribute Long adminId) throws Exception {
        return new APIResult<>(problemService.saveNewProblemByMachine(machineProblemModel, adminId));
    }

    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/uploadFile")
    public APIResult<Boolean> uploadProblemJudgeFile(@RequestParam("file") MultipartFile[] files, @RequestParam Long problemId) throws Exception {
        return new APIResult<>(problemService.uploadProblemJudgeFile(files, problemId));
    }

    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/uploadSpj")
    public APIResult<Boolean> uploadSpjProblemJudgeFile(@RequestParam("file") MultipartFile file, @RequestParam Long problemId) throws Exception {
        return new APIResult<>(problemService.uploadOtherProblemJudgeFile(file, problemId, ProblemType.SPJ));
    }

    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/uploadInsert")
    public APIResult<Boolean> uploadInsertProblemJudgeFile(@RequestParam("file") MultipartFile file, @RequestParam Long problemId) throws Exception {
        return new APIResult<>(problemService.uploadOtherProblemJudgeFile(file, problemId, ProblemType.FUNCTION));
    }
}

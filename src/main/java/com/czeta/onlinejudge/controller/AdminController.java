package com.czeta.onlinejudge.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.consts.FileConstant;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.dao.entity.Tag;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.*;
import com.czeta.onlinejudge.model.result.AppliedCertificationModel;
import com.czeta.onlinejudge.model.result.ProblemTagModel;
import com.czeta.onlinejudge.model.result.SimpleContestModel;
import com.czeta.onlinejudge.model.result.SimpleProblemModel;
import com.czeta.onlinejudge.service.*;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.czeta.onlinejudge.utils.utils.DownloadUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

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
    private TagService tagService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private MultipartProperties multipartProperties;

    @ApiOperation(value = "(用户)分页获得所有用户的详情信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=1)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/userInfoList")
    public APIResult<IPage<User>> getUserInfoList(@RequestBody PageModel pageModel) {
        return new APIResult<>(userService.getUserInfoList(pageModel));
    }

    @ApiOperation(value = "(用户)根据用户名关键字分页获取相关用户信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData为搜索用户名关键字", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=2)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/searchResult")
    public APIResult<IPage<User>> getUserSearchResult(@RequestBody PageModel<String> pageModel) {
        return new APIResult<>(userService.getUserInfosByUsernameKey(pageModel));
    }

    @ApiOperation(value = "(用户)添加新的用户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRegisterModel", value = "新的用户注册信息", dataType = "UserRegisterModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @ApiOperationSupport(order=3)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/save")
    public APIResult saveNewUser(@RequestBody UserRegisterModel userRegisterModel) {
        userService.saveNewUser(userRegisterModel);
        return new APIResult();
    }

    @ApiOperation(value = "(用户)重置用户密码为与用户名一致", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "重置密码的目标用户名", dataType = "String", paramType = "path", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=4)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/password/update/{username}")
    public APIResult<Boolean> updateUserPassword(@PathVariable String username) {
        return new APIResult<>(userService.resetUserPasswordByUsername(username));
    }

    @ApiOperation(value = "(用户)禁用/启用用户账户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "禁用/启用账户的目标用户名", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "status", value = "禁用为0，启用为1", dataType = "Short", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=5)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/account/update/{username}")
    public APIResult<Boolean> updateUserAccount(@PathVariable String username, @RequestParam Short status) {
        return new APIResult<>(userService.updateUserAccount(username, status));
    }


    @ApiOperation(value = "(用户)按条件批量注册用户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rangedUserModel", value = "注册条件", dataType = "RangedUserModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "失败"),
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @ApiOperationSupport(order=6)
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


    @ApiOperation(value = "(认证)获得用户申请的实名认证信息列表（待审核）", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=7)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/appliedCertificationList")
    public APIResult<IPage<AppliedCertificationModel>> getAppliedCertificationList(@RequestBody PageModel pageModel) {
        return new APIResult<>(certificationService.getAppliedCertificationList(pageModel));
    }


    @ApiOperation(value = "(认证)审核实名认证信息：通过或不通过", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "审核结果：1表示通过、-1表示不通过", dataType = "Short", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=8)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/userManager/appliedCertification/update")
    public APIResult<Boolean> updateAppliedCertification(@RequestParam Short status, @RequestParam Long userId) {
        return new APIResult<>(certificationService.updateAppliedCertificationByUserId(status, userId));
    }


    @ApiOperation(value = "(认证)获得实名认证类型列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @ApiOperationSupport(order=9)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/certManager/certTypeList")
    public APIResult<List<Certification>> getCertificationTypeList() {
        return new APIResult<>(certificationService.getCertificationTypes());
    }


    @ApiOperation(value = "(认证)确定最终实名认证类型列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeList", value = "实名认证类型列表", dataType = "List<String>", paramType = "body", required = true)})
    @ApiResponses({
            @ApiResponse(code = 2101, message = "已存在的名称")
    })
    @ApiOperationSupport(order=10)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/save")
    public APIResult saveAndUpdateCertification(@RequestBody List<String> typeList) {
        certificationService.saveAndUpdateCertification(typeList);
        return new APIResult();
    }


    @ApiOperation(value = "(认证)更新实名认证类型名", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certificationModel", value = "实名认证类型", dataType = "CertificationModel", paramType = "body", required = true)})
    @ApiResponses({})
    @ApiOperationSupport(order=11)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/certManager/update")
    public APIResult<Boolean> updateCertification(@RequestBody CertificationModel certificationModel) {
        return new APIResult<>(certificationService.updateCertification(certificationModel));
    }


    @ApiOperation(value = "(管理员)分页获取普通管理员详细信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=12)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/adminInfoList")
    public APIResult<IPage<Admin>> getAdminInfoList(@RequestBody PageModel pageModel) {
        return new APIResult<>(adminService.getAdminInfoList(pageModel));
    }



    @ApiOperation(value = "(管理员)根据管理员用户名关键字分页获得相关普通管理员账号信息", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData为用户名关键字", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=13)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/searchResult")
    public APIResult<IPage<Admin>> getAdminSearchResult(@RequestBody PageModel<String> pageModel) {
        return new APIResult<>(adminService.getAdminInfoListByUsernameKey(pageModel));
    }


    @ApiOperation(value = "(管理员)添加新的普通管理员账户", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminRegisterModel", value = "管理员注册基本信息", dataType = "AdminRegisterModel", paramType = "body", required = true)})
    @ApiResponses({
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @ApiOperationSupport(order=14)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/save")
    public APIResult saveNewAdmin(@RequestBody AdminRegisterModel adminRegisterModel) {
        adminService.saveNewAdmin(adminRegisterModel);
        return new APIResult();
    }


    @ApiOperation(value = "(管理员)重置普通管理员密码", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "重置密码的目标管理员用户名", dataType = "String", paramType = "path", required = true)})
    @ApiResponses({})
    @ApiOperationSupport(order=15)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/password/update/{username}")
    public APIResult<Boolean> updateAdminPassword(@PathVariable String username) {
        return new APIResult<>(adminService.resetAdminPasswordByUsername(username));
    }


    @ApiOperation(value = "(管理员)禁用/启用普通管理员账号", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "禁用/启用账户的目标管理员用户名", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "status", value = "禁用为0，启用为1", dataType = "Short", paramType = "query", required = true)})
    @ApiResponses({})
    @ApiOperationSupport(order=16)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/adminManager/account/update/{username}")
    public APIResult<Boolean> updateAdminAccount(@PathVariable String username, @RequestParam Short status) {
        return new APIResult<>(adminService.updateAdminAccount(username, status));
    }


    @ApiOperation(value = "(主页公告)分页获得主页公告信息列表", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=17)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/ancInfoList")
    public APIResult<IPage<Announcement>> getHomePageAnnouncementList(@RequestBody PageModel pageModel) {
        return new APIResult<>(announcementService.getHomePageAnnouncementList(pageModel));
    }



    @ApiOperation(value = "(主页公告)根据公告ID获取首页公告信息", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告ID", dataType = "Long", paramType = "path", required = true)})
    @ApiResponses({})
    @ApiOperationSupport(order=18)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @GetMapping("/ancManager/ancInfo/{id}")
    public APIResult<Announcement> getHomePageAnnouncement(@PathVariable Long id) {
        return new APIResult<>(announcementService.getAnnouncementInfoById(id));
    }


    @ApiOperation(value = "(主页公告)添加新的首页公告", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "添加的公告实体model", dataType = "AnnouncementModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=19)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/save")
    public APIResult saveNewHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel, @ApiIgnore @RequestAttribute Long userId) {
        announcementService.saveNewHomePageAnnouncement(announcementModel, userId);
        return new APIResult();
    }


    @ApiOperation(value = "(主页公告)更新首页公告", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "修改的公告实体model", dataType = "AnnouncementModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=20)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/ancManager/update")
    public APIResult<Boolean> updateHomePageAnnouncement(@RequestBody AnnouncementModel announcementModel) {
        return new APIResult<>(announcementService.updateHomePageAnnouncement(announcementModel));
    }

    @ApiOperation(value = "(评测服务)获得评测机信息列表", notes = "需要token：超级admin权限，或普通管理员权限（仅在创建题目时选择评测机）")
    @ApiImplicitParams({})
    @ApiResponses({})
    @ApiOperationSupport(order=23)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/judgeManager/judgeMachineList")
    public APIResult<List<JudgeType>> getJudgeMachineList() {
        return new APIResult<>(judgeService.getJudgeMachineList());
    }

    @ApiOperation(value = "(评测服务)获得爬虫信息列表", notes = "需要token：超级admin权限，或普通管理员权限（仅在创建题目时选择爬虫）")
    @ApiImplicitParams({})
    @ApiResponses({})
    @ApiOperationSupport(order=25)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/judgeManager/judgeSpiderList")
    public APIResult<List<JudgeType>> getJudgeSpiderList() {
        return new APIResult<>(judgeService.getJudgeSpiderList());
    }

    @ApiOperation(value = "(评测服务)更新评测状态", notes = "需要token：超级admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "评测信息状态，正常(1)、异常(-1)、停用(0)", dataType = "Short", paramType = "query", required = true),
            @ApiImplicitParam(name = "judgeTypeId", value = "评测信息ID", dataType = "Integer", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=26)
    @RequiresRoles(RoleType.Names.SUPER_ADMIN)
    @PostMapping("/judgeManager/judgeStatus/update")
    public APIResult<Boolean> updateJudgeStatusById(@RequestParam Short status, @RequestParam Integer judgeTypeId) {
        return new APIResult<>(judgeService.updateJudgeStatusById(status, judgeTypeId));
    }

    @ApiOperation(value = "(题目评测服务)获取去重后的NORMAL评测机列表（种类去重）", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @ApiOperationSupport(order=27)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/judgeManager/uniqueJudgeMachineList")
    public APIResult<List<JudgeType>> getUniqueJudgeMachineList() {
        return new APIResult<>(judgeService.getUniqueJudgeMachineList());
    }


    @ApiOperation(value = "(题目标签)添加新的题目标签", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagName", value = "标签名", dataType = "String", paramType = "query", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2101, message = "名称已存在")
    })
    @ApiOperationSupport(order=28)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/tag/save")
    public APIResult saveNewTag(@RequestParam String tagName, @ApiIgnore @RequestAttribute Long userId) {
        tagService.saveNewTag(tagName, userId);
        return new APIResult();
    }


    @ApiOperation(value = "(题目标签)获取平台所有标签", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @ApiOperationSupport(order=29)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/problemManager/tags")
    public APIResult<List<Tag>> getTagInfoList() {
        return new APIResult<>(tagService.getTagInfoList());
    }


    @ApiOperation(value = "(题目标签)获取题目所属标签列表", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "题目ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=30)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/problemManager/problemInfo/tags")
    public APIResult<List<ProblemTagModel>> getProblemTagByProblemId(@RequestParam Long problemId) {
        return new APIResult<>(tagService.getProblemTagByProblemId(problemId));
    }


    @ApiOperation(value = "(题目)创建爬虫评测的题目", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spiderProblemModel", value = "爬虫题目model", dataType = "SpiderProblemModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2101, message = "名称已存在")
    })
    @ApiOperationSupport(order=31)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/spider/save")
    public APIResult<Long> saveNewProblemBySpider(@RequestBody SpiderProblemModel spiderProblemModel, @ApiIgnore @RequestAttribute Long userId) throws Exception {
        return new APIResult<>(problemService.saveNewProblemBySpider(spiderProblemModel, userId));
    }


    @ApiOperation(value = "(题目)创建评测机评测的题目", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "machineProblemModel", value = "评测机评测的题目model", dataType = "MachineProblemModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2101, message = "名称已存在"),
            @ApiResponse(code = 2103, message = "该题已设置评测方式")
    })
    @ApiOperationSupport(order=32)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/machine/save")
    public APIResult<Long> saveNewProblemByMachine(@RequestBody MachineProblemModel machineProblemModel, @ApiIgnore @RequestAttribute Long userId) throws Exception {
        return new APIResult<>(problemService.saveNewProblemByMachine(machineProblemModel, userId));
    }


    @ApiOperation(value = "(题目)获取评测机/爬虫评测的题目信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "题目ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=33)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/problemManager/problemInfo")
    public APIResult<MachineProblemModel> getProblemInfo(@RequestParam Long problemId) {
        return new APIResult<>(problemService.getProblemInfo(problemId));
    }


    @ApiOperation(value = "(题目)更新评测机评测的题目信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "machineProblemModel", value = "评测机评测的题目信息", dataType = "MachineProblemModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=34)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/problemInfo/update")
    public APIResult<Boolean> updateProblemInfoOfMachine(@RequestBody MachineProblemModel machineProblemModel, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(problemService.updateProblemInfoOfMachine(machineProblemModel, machineProblemModel.getId(), userId));
    }


    @ApiOperation(value = "(题目)更新题目状态信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "更新的题目ID", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "更新的状态：1表示正常并可视，0表示正常但不可视（即只能在比赛界面看到该题，不能在题目模块看到该题），-1表示题目禁用", dataType = "Short", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=34)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/problemStatus/update")
    public APIResult<Boolean> updateProblemStatus(@RequestParam Long problemId, @RequestParam Short status, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(problemService.updateProblemStatus(status, problemId, userId));
    }



    @ApiOperation(value = "(题目)分页获取题目信息列表（简易）", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页信息，paramData为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=35)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/problemList")
    public APIResult<IPage<SimpleProblemModel>> getSimpleProblemList(@RequestBody PageModel page) {
        return new APIResult<>(problemService.getSimpleProblemList(page));
    }

    @ApiOperation(value = "(题目)根据题目关键字分页获取题目信息列表（简易）", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页请求参数，这里的paramData为搜索题目标题关键字", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=35)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/searchResult")
    public APIResult<IPage<SimpleProblemModel>> getProblemSearchResult(@RequestBody PageModel<String> page) {
        return new APIResult<>(problemService.getSimpleProblemListByTitleKey(page));
    }


    @ApiOperation(value = "(题目文件)上传题目评测数据：zip文件格式", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "评测文件，规则：(1)如果不是spj，则文件名需要成对出现，并且文件名一致（从1开始递增），后缀分别是in和out；\\n\" +\n" +
                    "            \"(2)如果是则文件名单个出现，并且文件名从1开始递增，后缀是in\"", dataType = "MultipartFile", paramType = "query", required = true),
            @ApiImplicitParam(name = "spj", value = "是否特判", dataType = "Boolean", paramType = "query", required = true),
            @ApiImplicitParam(name = "problemId", value = "题目ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "文件校验失败：如果是!spj：文件名需要成对出现，并且文件名一致（从1开始递增），后缀分别是in和out；如果是spj：文件名单个出现，并且文件名单个出现，并且文件名从1开始递增，后缀是in")
    })
    @ApiOperationSupport(order=36)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/problemManager/uploadFile")
    public APIResult<Boolean> uploadTestCaseZip(@RequestParam MultipartFile file, @RequestParam Boolean spj, @RequestParam Long problemId, @ApiIgnore @RequestAttribute Long userId) throws Exception {
        return new APIResult<>(problemService.uploadTestCaseZip(file, spj, problemId, userId));
    }


    @ApiOperation(value = "(题目文件)下载zip评测文件", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "题目ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=39)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/problemManager/downloadFile")
    public void downloadTestCaseZip(@RequestParam Long problemId, HttpServletResponse response) throws Exception {
        problemService.downloadTestCaseZip(problemId, response);
    }


    @ApiOperation(value = "(题目文件)获取评测文件名列表", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "题目ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=40)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/problemManager/files")
    public APIResult<List<String>> getTestCaseFileList(@RequestParam Long problemId) {
        return new APIResult<>(problemService.getTestCaseFileList(problemId));
    }


    @ApiOperation(value = "(比赛)创建比赛", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestModel", value = "创建比赛的model", dataType = "ContestModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "报名模式不存在"),
            @ApiResponse(code = 2001, message = "排名模式不存在"),
            @ApiResponse(code = 2102, message = "题目已存在")
    })
    @ApiOperationSupport(order=44)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/save")
    public APIResult saveNewContest(@RequestBody ContestModel contestModel, @ApiIgnore @RequestAttribute Long userId) {
        contestService.saveNewContest(contestModel, userId);
        return new APIResult();
    }


    @ApiOperation(value = "(比赛)更新比赛信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestModel", value = "更新比赛的model", dataType = "ContestModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "报名模式不存在"),
            @ApiResponse(code = 2001, message = "排名模式不存在"),
            @ApiResponse(code = 2001, message = "比赛已经封榜或弃用，不能更新"),
            @ApiResponse(code = 2001, message = "比赛已开始，不能更新"),
            @ApiResponse(code = 2001, message = "开始时间不能设置为当前时间之前"),
            @ApiResponse(code = 2001, message = "结束时间不能设置为开始时间之前")
    })
    @ApiOperationSupport(order=45)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/update")
    public APIResult<Boolean> updateContestInfo(@RequestBody ContestModel contestModel, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.updateContestInfo(contestModel, contestModel.getId(), userId));
    }


    @ApiOperation(value = "(比赛)积分赛封榜", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "比赛不存在"),
            @ApiResponse(code = 2001, message = "比赛尚未结束"),
            @ApiResponse(code = 2302, message = "该比赛不是积分赛"),
            @ApiResponse(code = 2301, message = "比赛已经封榜结束(已经计算过积分)")
    })
    @ApiOperationSupport(order=46)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/block")
    public APIResult calculateRatingDataOfRatingContest(@RequestParam Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        contestService.calculateRatingDataOfRatingContest(contestId, userId);
        return new APIResult();
    }


    @ApiOperation(value = "(比赛)按条件获取比赛信息列表（简易）", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页信息，paramData为比赛名关键字，为空时则返回全部比赛列表", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=47)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/contestList")
    public APIResult<IPage<SimpleContestModel>> getSimpleContestList(@RequestBody PageModel<String> pageModel) {
        return new APIResult<>(contestService.getSimpleContestList(pageModel));
    }


    @ApiOperation(value = "(比赛)获取比赛详细信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=48)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/contestManager/contestInfo")
    public APIResult<Contest> getContestInfo(@RequestParam Long contestId) {
        return new APIResult<>(contestService.getContestInfo(contestId));
    }


    @ApiOperation(value = "(比赛题目)获取比赛的题目ID列表", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=49)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/contestManager/problemIdList")
    public APIResult<List<Long>> getProblemListOfContest(@RequestParam Long contestId) {
        return new APIResult<>(contestService.getProblemListOfContest(contestId));
    }


    @ApiOperation(value = "(比赛用户)获取比赛的申请报名的用户列表", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页信息，这里的paramData为null", dataType = "PageModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=50)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/contestUserList")
    public APIResult<IPage<ContestUser>> getAppliedContestUserList(@RequestBody PageModel pageModel, @RequestParam Long contestId) {
        return new APIResult<>(contestService.getAppliedContestUserList(pageModel, contestId));
    }


    @ApiOperation(value = "(比赛用户)审核申请报名比赛的用户", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "审核状态：0表示尚未审核、1表示审核通过、-1表示审核不通过", dataType = "Short", paramType = "query", required = true),
            @ApiImplicitParam(name = "id", value = "申请报名比赛的用户model的ID", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=51)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/contestUser/update")
    public APIResult<Boolean> updateAppliedContestUser(@RequestParam Short status, @RequestParam Long id,
                                                       @RequestParam Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(contestService.updateAppliedContestUser(status, id, contestId, userId));
    }


    @ApiOperation(value = "(比赛公告)新增比赛公告", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "公告model", dataType = "AnnouncementModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=52)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/announcement/save")
    public APIResult saveNewContestAnnouncement(@RequestBody AnnouncementModel announcementModel, @RequestParam Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        announcementService.saveNewContestAnnouncement(announcementModel, contestId, userId);
        return new APIResult();
    }


    @ApiOperation(value = "(比赛公告)更新比赛指定公告", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "announcementModel", value = "公告model", dataType = "AnnouncementModel", paramType = "body", required = true),
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=53)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @PostMapping("/contestManager/announcement/update")
    public APIResult<Boolean> updateContestAnnouncement(@RequestBody AnnouncementModel announcementModel, @RequestParam Long contestId, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(announcementService.updateContestAnnouncement(announcementModel, contestId, userId));
    }


    @ApiOperation(value = "(比赛公告)获取比赛的公告列表", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contestId", value = "比赛ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=54)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/contestManager/announcementList")
    public APIResult<List<Announcement>> getContestAnnouncementList(@RequestParam Long contestId) {
        return new APIResult<>(announcementService.getContestAnnouncementList(contestId));
    }


    @ApiOperation(value = "(比赛公告)获取比赛公告详细信息", notes = "需要token：超级admin权限 or 普通admin权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告ID", dataType = "Long", paramType = "query", required = true)
    })
    @ApiResponses({})
    @ApiOperationSupport(order=55)
    @RequiresRoles(value = {RoleType.Names.SUPER_ADMIN, RoleType.Names.COMMON_ADMIN}, logical = Logical.OR)
    @GetMapping("/contestManager/announcementInfo")
    public APIResult<Announcement> getAnnouncementInfoById(@RequestParam Long id) {
        return new APIResult<>(announcementService.getAnnouncementInfoById(id));
    }
}

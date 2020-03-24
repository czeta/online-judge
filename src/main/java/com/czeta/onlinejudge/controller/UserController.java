package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.consts.FileConstant;
import com.czeta.onlinejudge.dao.entity.Certification;
import com.czeta.onlinejudge.dao.entity.Message;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.UserRegisterModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;
import com.czeta.onlinejudge.service.CertificationService;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.czeta.onlinejudge.utils.utils.DownloadUtils;
import com.czeta.onlinejudge.utils.utils.UploadUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName UserController
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 14:20
 * @Version 1.0
 */

@Api(tags = "User Features Controller")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private MultipartProperties multipartProperties;

    @ApiOperation(value = "新用户注册", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRegisterModel", value = "注册实体，三个属性必填", dataType = "UserRegisterModel", paramType= "body", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 2100, message = "用户名已存在")
    })
    @PostMapping("/register")
    public APIResult saveNewUser(@RequestBody UserRegisterModel userRegisterModel) {
        userService.saveNewUser(userRegisterModel);
        return new APIResult();
    }



    @ApiOperation(value = "根据userId获取用户主页信息", notes = "这一个复用controller：传id查看别人主页，不传任何参数则查看自己主页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id，查看别人的主页所需要传的参数", dataType = "Long", paramType= "query", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己的主页传的参数，不过这是解析token得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 1004, message = "返回值校验失败：没有该用户数据")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/userInfo")
    public APIResult<User> getUserInfo(@RequestParam(value = "id", required = false) Long id, @ApiIgnore @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getUserInfoById(id));
    }

    @ApiOperation(value = "根据userId获得用户解决的题号列表", notes = "这一个复用controller：传id查看别人解决的题号列表，不传任何参数则查看自己解决的题号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id，查看别人解决的题号列表所需要传的参数", dataType = "Long", paramType= "query", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己解决的题号列表传的参数，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/solvedProblems")
    public APIResult<List<Long>> getSolvedProblems(@RequestParam(value = "id", required = false) Long id, @ApiIgnore @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getSolvedProblemsByUserId(id));
    }

    @ApiOperation(value = "根据userId获得用户尝试过但未解决的题号列表", notes = "这一个复用controller：传id查看别人尝试过但未解决的题号列表，不传任何参数则查看自己尝试过但未解决的题号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id，查看别人尝试过但未解决的题号列表所需要传的参数", dataType = "Long", paramType= "query", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己尝试过但未解决的题号列表传的参数，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/notSolvedProblems")
    public APIResult<List<Long>> getNotSolvedProblems(@RequestParam(value = "id", required = false) Long id, @ApiIgnore @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getNotSolvedProblemsByUserId(id));
    }

    @ApiOperation(value = "根据userId分页获得用户的消息列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType= "body", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，查看消息详情列表，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/messages")
    public APIResult<IPage<Message>> getMessages(@RequestBody PageModel page, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(userService.getMessagesByUserId(page, userId));
    }

    @ApiOperation(value = "根据消息Id修改message状态：未读->已读", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgId", value = "消息ID", dataType = "Long", paramType= "path", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/message/update/{msgId}")
    public APIResult<Boolean> updateMessageStatus(@PathVariable Long msgId) {
        return new APIResult<>(userService.updateMessageStatusById(msgId));
    }

    @ApiOperation(value = "根据userId更新用户基本信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfoModel", value = "用户修改后的信息实体", dataType = "UserInfoModel", paramType= "body", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userInfo/update")
    public APIResult<Boolean> updateUserInfo(@RequestBody UserInfoModel userInfoModel, @ApiIgnore @RequestAttribute Long userId) {
        userInfoModel.setId(userId);
        return new APIResult(userService.updateUserInfoByUserId(userInfoModel));
    }

    @ApiOperation(value = "根据userId更新用户邮箱", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldEmail", value = "用户输入的旧邮箱", dataType = "String", paramType= "query", required = true),
            @ApiImplicitParam(name = "newEmail", value = "用户输入的新邮箱", dataType = "String", paramType= "query", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userEmail/update")
    public APIResult<Boolean> updateUserEmail(@RequestParam String oldEmail, @RequestParam String newEmail, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserEmailByUserId(oldEmail, newEmail, userId));
    }

    @ApiOperation(value = "根据userId更新用户密码", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "用户输入的旧密码", dataType = "String", paramType= "query", required = true),
            @ApiImplicitParam(name = "newPassword", value = "用户输入的新密码", dataType = "String", paramType= "query", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userPwd/update")
    public APIResult<Boolean> updateUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @ApiIgnore @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserPasswordByUserId(oldPassword, newPassword, userId));
    }


    @ApiOperation(value = "获得实名认证类型列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({})
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/userCertification/certTypeList")
    public APIResult<List<Certification>> getCertificationTypeList() {
        return new APIResult<>(certificationService.getValidCertificationTypes());
    }

    @ApiOperation(value = "根据userId，申请实名认证", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCertificationModel", value = "用户申请认证的信息实体", dataType = "UserCertificationModel", paramType= "body", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userCertification/save")
    public APIResult saveNewCertification(@RequestBody UserCertificationModel userCertificationModel, @ApiIgnore @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        certificationService.saveNewCertification(userCertificationModel);
        return new APIResult();
    }

    @ApiOperation(value = "根据userId，修改实名认证信息并重新申请", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCertificationModel", value = "用户申请认证的信息实体", dataType = "UserCertificationModel", paramType= "body", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userCertification/update")
    public APIResult<Boolean> updateUserCertification(@RequestBody UserCertificationModel userCertificationModel, @ApiIgnore @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        return new APIResult(certificationService.updateUserCertification(userCertificationModel));
    }

    @ApiOperation(value = "根据userId，获得已申请的实名认证信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/userCertification")
    public APIResult<UserCertification> getUserCertification(@ApiIgnore @RequestAttribute Long userId) {
        return new APIResult(certificationService.getUserCertification(userId));
    }

    @ApiOperation(value = "上传头像", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "head", value = "二进制文件", dataType = "MultipartFile", paramType= "query", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", paramType= "body", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "失败")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/uploadHead")
    public APIResult<Boolean> uploadHead(@RequestParam("head") MultipartFile head, @ApiIgnore @RequestAttribute Long userId) throws Exception{
        return new APIResult<>(userService.uploadHeadImage(head, userId));
    }


    @ApiOperation(value = "获取头像", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "headImageName", value = "头像文件名", dataType = "String", paramType= "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "失败")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/head/{headImageName}")
    public void getImage(@PathVariable String headImageName, HttpServletResponse response) throws IOException{
        // 重定向到图片访问路径
        response.sendRedirect(multipartProperties.getResourceAccessPath() + headImageName);
    }

    @ApiOperation(value = "下载头像", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "downloadFileName", value = "头像文件名", dataType = "String", paramType= "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 1001, message = "失败")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/downloadHead/{downloadFileName}")
    public void download(@PathVariable String downloadFileName, HttpServletResponse response) throws Exception{
        userService.downloadHeadImage(downloadFileName, response);
    }
}

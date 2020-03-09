package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.czeta.onlinejudge.util.response.APIResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "新用户注册", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRegisterModel", value = "注册实体，三个属性必填", dataType = "UserRegisterModel", required = false)
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
            @ApiImplicitParam(name = "id", value = "用户id，查看别人的主页所需要传的参数", dataType = "Long", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己的主页传的参数，不过这是解析token得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 1004, message = "返回值校验失败：没有该用户数据")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/userInfo")
    public APIResult<User> getUserInfo(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getUserInfoById(id));
    }

    @ApiOperation(value = "根据userId获得用户解决的题号列表", notes = "这一个复用controller：传id查看别人解决的题号列表，不传任何参数则查看自己解决的题号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id，查看别人解决的题号列表所需要传的参数", dataType = "Long", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己解决的题号列表传的参数，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/solvedProblems")
    public APIResult<List<Long>> getSolvedProblems(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getSolvedProblemsByUserId(id));
    }

    @ApiOperation(value = "根据userId获得用户尝试过但未解决的题号列表", notes = "这一个复用controller：传id查看别人尝试过但未解决的题号列表，不传任何参数则查看自己尝试过但未解决的题号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id，查看别人尝试过但未解决的题号列表所需要传的参数", dataType = "Long", required = false),
            @ApiImplicitParam(name = "userId", value = "用户id，查看自己尝试过但未解决的题号列表传的参数，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/notSolvedProblems")
    public APIResult<List<Long>> getNotSolvedProblems(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getNotSolvedProblemsByUserId(id));
    }

    @ApiOperation(value = "根据userId分页获得用户的消息列表", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，查看消息详情列表，不过这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/messages")
    public APIResult<IPage<Message>> getMessages(@RequestBody PageModel page, @RequestAttribute Long userId) {
        return new APIResult<>(userService.getMessagesByUserId(page, userId));
    }

    @ApiOperation(value = "根据消息Id修改message状态：未读->已读", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgId", value = "消息ID", dataType = "Long", required = true)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/message/update/{msgId}")
    public APIResult<Boolean> updateMessageStatus(@PathVariable Long msgId) {
        return new APIResult<>(userService.updateMessageStatusById(msgId));
    }

    @ApiOperation(value = "根据userId更新用户基本信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfoModel", value = "用户修改后的信息实体", dataType = "UserInfoModel", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userInfo/update")
    public APIResult<Boolean> updateUserInfo(@RequestBody UserInfoModel userInfoModel, @RequestAttribute Long userId) {
        userInfoModel.setId(userId);
        return new APIResult(userService.updateUserInfoByUserId(userInfoModel));
    }

    @ApiOperation(value = "根据userId更新用户邮箱", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldEmail", value = "用户输入的旧邮箱", dataType = "String", required = true),
            @ApiImplicitParam(name = "newEmail", value = "用户输入的新邮箱", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userEmail/update")
    public APIResult<Boolean> updateUserEmail(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserEmailByUserId(oldEmail, newEmail, userId));
    }

    @ApiOperation(value = "根据userId更新用户密码", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "用户输入的旧密码", dataType = "String", required = true),
            @ApiImplicitParam(name = "newPassword", value = "用户输入的新密码", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userPwd/update")
    public APIResult<Boolean> updateUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserPasswordByUserId(oldPassword, newPassword, userId));
    }

    @ApiOperation(value = "根据userId，申请实名认证", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCertificationModel", value = "用户申请认证的信息实体", dataType = "UserCertificationModel", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userCertification/save")
    public APIResult saveNewCertification(@RequestBody UserCertificationModel userCertificationModel, @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        certificationService.saveNewCertification(userCertificationModel);
        return new APIResult();
    }

    @ApiOperation(value = "根据userId，修改实名认证信息并重新申请", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCertificationModel", value = "用户申请认证的信息实体", dataType = "UserCertificationModel", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/userCertification/update")
    public APIResult<Boolean> updateUserCertification(@RequestBody UserCertificationModel userCertificationModel, @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        return new APIResult(certificationService.updateUserCertification(userCertificationModel));
    }

    @ApiOperation(value = "根据userId，获得已申请的实名认证信息", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id，这是解析token自动得出的，故不需要传入此参数", dataType = "Long", required = false)
    })
    @ApiResponses({})
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @GetMapping("/userCertification")
    public APIResult<UserCertification> getUserCertification(@RequestAttribute Long userId) {
        return new APIResult(certificationService.getUserCertification(userId));
    }

}

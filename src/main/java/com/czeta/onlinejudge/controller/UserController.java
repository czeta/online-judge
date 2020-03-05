package com.czeta.onlinejudge.controller;

import com.czeta.onlinejudge.dao.entity.Message;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.entity.UserCertification;
import com.czeta.onlinejudge.model.param.UserRegisterModel;
import com.czeta.onlinejudge.model.param.UserCertificationModel;
import com.czeta.onlinejudge.model.param.UserInfoModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.util.response.APIResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public APIResult saveNewUser(@RequestBody UserRegisterModel userRegisterModel) {
        userService.saveNewUser(userRegisterModel);
        return new APIResult();
    }

    @GetMapping("/userInfo")
    public APIResult<User> getUserInfo(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getUserInfoById(id));
    }

    @GetMapping("/solvedProblems")
    public APIResult<List<Long>> getSolvedProblems(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getSolvedProblemsByUserId(id));
    }

    @GetMapping("/notSolvedProblems")
    public APIResult<List<Long>> getNotSolvedProblems(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getNotSolvedProblemsByUserId(id));
    }

    @GetMapping("/messages")
    public APIResult<List<Message>> getMessages(@RequestAttribute Long userId) {
        return new APIResult<>(userService.getMessagesByUserId(userId));
    }

    @PostMapping("/userInfo/update")
    public APIResult<Boolean> updateUserInfo(@RequestBody UserInfoModel userInfoModel, @RequestAttribute Long userId) {
        userInfoModel.setId(userId);
        return new APIResult(userService.updateUserInfoByUserId(userInfoModel));
    }

    @PostMapping("/userEmail/update")
    public APIResult<Boolean> updateUserEmail(@RequestParam String oldEmail, @RequestParam String newEmail, @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserEmailByUserId(oldEmail, newEmail, userId));
    }

    @PostMapping("/userPwd/update")
    public APIResult<Boolean> updateUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestAttribute Long userId) {
        return new APIResult<>(userService.updateUserPasswordByUserId(oldPassword, newPassword, userId));
    }

    @PostMapping("/userCertification/save")
    public APIResult saveNewCertification(@RequestBody UserCertificationModel userCertificationModel, @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        userService.saveNewCertification(userCertificationModel);
        return new APIResult();
    }

    @PostMapping("/userCertification/update")
    public APIResult<Boolean> updateUserCertification(@RequestBody UserCertificationModel userCertificationModel, @RequestAttribute Long userId) {
        userCertificationModel.setUserId(userId);
        return new APIResult(userService.updateUserCertification(userCertificationModel));
    }

    @GetMapping("/userCertification")
    public APIResult<UserCertification> getUserCertification(@RequestAttribute Long userId) {
        return new APIResult(userService.getUserCertification(userId));
    }

}

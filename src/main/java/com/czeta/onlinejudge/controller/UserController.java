package com.czeta.onlinejudge.controller;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.model.param.RegisterParamModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.util.response.APIResult;
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
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public APIResult saveNewUser(@RequestBody RegisterParamModel registerParamModel) {
        userService.saveNewUser(registerParamModel);
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
    public APIResult<List<Long>> getSolvedProblem(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getSolvedProblemByUserId(id));
    }

    @GetMapping("/notSolvedProblems")
    public APIResult<List<Long>> getNotSolvedProblem(@RequestParam(value = "id", required = false) Long id, @RequestAttribute Long userId) {
        if (id == null && userId != null) {
            id = userId;
        }
        return new APIResult<>(userService.getNotSolvedProblemByUserId(id));
    }
}

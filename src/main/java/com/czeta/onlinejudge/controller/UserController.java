package com.czeta.onlinejudge.controller;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.model.param.RegisterParamModel;
import com.czeta.onlinejudge.service.UserService;
import com.czeta.onlinejudge.util.response.APIResult;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/userInfo/{id}")
    public APIResult<User> getUserInfoById(@PathVariable(value = "id") Long id) {
        AssertUtils.notNull(id, BaseStatusMsg.APIEnum.PARAM_ERROR);
        return new APIResult<>(userService.getUserInfoById(id));
    }
}

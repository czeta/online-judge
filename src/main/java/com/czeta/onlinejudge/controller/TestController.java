package com.czeta.onlinejudge.controller;

import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public List<User> hello() {
        return userMapper.selectList(null);
    }
}

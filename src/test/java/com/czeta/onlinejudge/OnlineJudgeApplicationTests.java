package com.czeta.onlinejudge;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.RoleMapper;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
class OnlineJudgeApplicationTests {
    private MockMvc mvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Before
    public void setUp() {
    }

    @Test
    void contextLoads() {
    }

    @Test
    void user() {
        System.out.println(PasswordUtils.encrypt("123123", jwtProperties.getSecret()));
    }
}
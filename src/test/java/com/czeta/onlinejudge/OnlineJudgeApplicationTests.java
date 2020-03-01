package com.czeta.onlinejudge;

import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
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
    private JwtProperties jwtProperties;

    @Before
    public void setUp() {
    }

    @Test
    void contextLoads() {
    }

    @Test
    void user() {
        System.out.println();
    }
}
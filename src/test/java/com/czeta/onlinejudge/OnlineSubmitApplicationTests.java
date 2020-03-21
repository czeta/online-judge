package com.czeta.onlinejudge;

import com.czeta.onlinejudge.config.MultipartProperties;
import com.czeta.onlinejudge.dao.mapper.UserCertificationMapper;
import com.czeta.onlinejudge.model.param.UserRegisterModel;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class OnlineSubmitApplicationTests {
    private MockMvc mvc;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserCertificationMapper userCertificationMapper;

    @Autowired
    private MultipartProperties multipartProperties;

    @Autowired
    private ProblemService problemService;

    @Before
    public void setUp() {
    }

    @Test
    void contextLoads() {
    }

    @Test
    void user() {
        System.out.println(DateUtils.getSecondDiffOfTwoDateString("2020-03-21 19:00:30", DateUtils.getYYYYMMDDHHMMSS(new Date())));
    }

    private List<UserRegisterModel> data() {
        List<UserRegisterModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserRegisterModel data = new UserRegisterModel();
            data.setUsername("user" + i);
            data.setPassword("user" + i);
            data.setEmail("user" + i + "@qq.com");
            list.add(data);
        }
        return list;
    }
}
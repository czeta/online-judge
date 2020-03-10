package com.czeta.onlinejudge;

import com.alibaba.excel.EasyExcel;
import com.czeta.onlinejudge.dao.mapper.UserCertificationMapper;
import com.czeta.onlinejudge.model.param.UserRegisterModel;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class OnlineJudgeApplicationTests {
    private MockMvc mvc;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserCertificationMapper userCertificationMapper;

    @Before
    public void setUp() {
    }

    @Test
    void contextLoads() {
    }

    @Test
    void user() {
        String fileName = "/opt/upload/excel_" + 3 + ".xlsx";
        EasyExcel.write(fileName, UserRegisterModel.class).sheet("模板").doWrite(data());
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
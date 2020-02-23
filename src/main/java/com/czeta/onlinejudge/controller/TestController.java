package com.czeta.onlinejudge.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description 测试控制器
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@RestController
public class TestController {

    @GetMapping("/")
    public String index() {
        log.info("test");
        return "index";
    }

    @RequiresGuest
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}

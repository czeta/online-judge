package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName RegisterParamModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 14:59
 * @Version 1.0
 */
@Data
public class RegisterParamModel {
    private String username;
    private String password;
    private String email;
}

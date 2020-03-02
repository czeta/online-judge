package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName UserAccountModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 11:36
 * @Version 1.0
 */
@Data
public class UserAccountModel {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private String oldEmail;
    private String newEmail;
}

package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName UserCertificationModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/3 17:13
 * @Version 1.0
 */
@Data
public class AppliedCertificationModel {
    private Long id;
    private Long userId;
    private String username;
    private String name;

    private String realName;
    private String sex;
    private Integer stuId;
    private String school;
    private String faculty;
    private String major;
    private String class0;
    private String phone;
    private String graduationTime;

    private Short status;
    private String crtTs;
    private String lmTs;
}

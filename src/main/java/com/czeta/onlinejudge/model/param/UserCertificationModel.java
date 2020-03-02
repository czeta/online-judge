package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName UserCertificationModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:48
 * @Version 1.0
 */
@Data
public class UserCertificationModel {
    private Long userId;
    private Integer certificationId;
    private String realName;
    private String sex;
    private Integer stuId;
    private String school;
    private String faculty;
    private String major;
    private String class0;
    private String phone;
    private String graduationTime;
}

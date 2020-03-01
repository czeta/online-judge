package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @ClassName UserCertification
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 12:43
 * @Version 1.0
 */
@Data
public class UserCertification {
    private Long id;
    private Long userId;
    private Integer certification;
    private String realName;
    private String sex;
    private Integer stuId;
    private String school;
    private String faculty;
    private String major;
    @TableField("`class`")
    private String class0;
    private String phone;
    private String graduationTime;
    private Short status;
    private String crtTs;
    private String lmTs;
}

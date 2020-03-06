package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserCertificationModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 14:48
 * @Version 1.0
 */
@ApiModel(description = "用户申请的实名认证实体Model")
@Data
public class UserCertificationModel {
    @ApiModelProperty(value = "申请认证的用户ID，这里置为null，由后台填充")
    private Long userId;
    @ApiModelProperty(value = "实名认证类型的ID")
    private Integer certificationId;
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "学号")
    private Integer stuId;
    @ApiModelProperty(value = "学校")
    private String school;
    @ApiModelProperty(value = "学院")
    private String faculty;
    @ApiModelProperty(value = "专业")
    private String major;
    @ApiModelProperty(value = "班级")
    private String class0;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "毕业时间")
    private String graduationTime;
}

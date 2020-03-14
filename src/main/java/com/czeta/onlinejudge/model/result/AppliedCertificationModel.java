package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserCertificationModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/3 17:13
 * @Version 1.0
 */
@ApiModel(description = "用户申请的实名认证审核model")
@Data
public class AppliedCertificationModel {
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "申请的用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "申请的实名认证类型")
    private String name;
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "学号")
    private String stuId;
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
    @ApiModelProperty(value = "认证状态：0尚未审核、1通过、-1不通过")
    private Short status;
    @ApiModelProperty(value = "申请时间")
    private String crtTs;
    @ApiModelProperty(value = "申请认证信息最后修改时间")
    private String lmTs;
}

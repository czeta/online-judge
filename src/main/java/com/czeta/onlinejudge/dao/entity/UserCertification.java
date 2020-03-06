package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserCertification
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 12:43
 * @Version 1.0
 */
@ApiModel(description = "用户认证实体")
@Data
public class UserCertification {
    @ApiModelProperty(value = "认证ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "认证ID")
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
    @TableField("`class`")
    private String class0;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "毕业时间")
    private String graduationTime;
    @ApiModelProperty(value = "认证状态：0表示尚未审核、1表示审核通过、-1表示审核不通过或者申请的认证类型失效，需要重新申请")
    private Short status;
    @ApiModelProperty(value = "认证申请时间")
    private String crtTs;
    @ApiModelProperty(value = "认证申请最后修改时间")
    private String lmTs;
}

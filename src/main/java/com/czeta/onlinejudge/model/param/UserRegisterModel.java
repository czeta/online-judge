package com.czeta.onlinejudge.model.param;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RegisterParamModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 14:59
 * @Version 1.0
 */
@ApiModel(description = "用户注册信息实体Model")
@Data
public class UserRegisterModel {
    @ApiModelProperty(value = "用户名")
    @ExcelProperty("用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    @ExcelProperty("密码（随机密码，请自行修改）")
    private String password;
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty("邮箱(临时邮箱，请自行修改)")
    private String email;
}

package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AdminRegisterModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 10:54
 * @Version 1.0
 */
@ApiModel(description = "管理员注册信息model")
@Data
public class AdminRegisterModel {
    @ApiModelProperty(value = "管理员用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "账号描述")
    private String description;
}

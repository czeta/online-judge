package com.czeta.onlinejudge.shiro.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @ClassName LoginParamModel
 * @Description 登录参数
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@ApiModel(description = "登录参数Model")
@Data
public class LoginParamModel {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;

}

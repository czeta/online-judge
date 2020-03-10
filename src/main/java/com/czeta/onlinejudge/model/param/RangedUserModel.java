package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RangedUserModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/10 14:26
 * @Version 1.0
 */
@ApiModel(description = "注册用户的条件model")
@Data
public class RangedUserModel {
    @ApiModelProperty(value = "用户名前缀")
    private String prefix;
    @ApiModelProperty(value = "用户名后缀")
    private String suffix;
    @ApiModelProperty(value = "用户名数字开始数字")
    private Integer startNumber;
    @ApiModelProperty(value = "用户名数字结束数字")
    private Integer endNumber;
    @ApiModelProperty(value = "密码长度，这里会随机生成指定长度的字母和数字混合密码串")
    private Integer passwordLength;
}

package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserInfoParamModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/2 11:08
 * @Version 1.0
 */
@ApiModel(description = "用户基本消息实体Model")
@Data
public class UserInfoModel {
    @ApiModelProperty(value = "用户ID，这里不需要填，由后台根据token自动填充")
    private Long id;
    @ApiModelProperty(value = "github地址")
    private String github;
    @ApiModelProperty(value = "博客地址")
    private String blog;
    @ApiModelProperty(value = "个性签名")
    private String mood;
}
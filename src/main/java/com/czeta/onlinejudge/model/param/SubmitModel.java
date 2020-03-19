package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SubmitModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 16:16
 * @Version 1.0
 */
@ApiModel(description = "用户提交代码model")
@Data
public class SubmitModel {
    @ApiModelProperty(value = "提交代码的问题ID")
    private Long problemId;
    @ApiModelProperty(value = "提交的代码")
    private String code;
    @ApiModelProperty(value = "提交代码的语言")
    private String language;
}

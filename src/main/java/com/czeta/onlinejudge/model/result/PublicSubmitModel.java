package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PublicSubmitModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:20
 * @Version 1.0
 */
@ApiModel(description = "公共提交评测model")
@Data
public class PublicSubmitModel {
    @ApiModelProperty(value = "提交ID")
    private Long id;
    @ApiModelProperty(value = "提交时间")
    private String crtTs;
    @ApiModelProperty(value = "提交评测状态：Accepted等...")
    private String submitStatus;
    @ApiModelProperty(value = "问题ID")
    private Long problemId;
    @ApiModelProperty(value = "运行时间")
    private String time;
    @ApiModelProperty(value = "运行内存")
    private String memory;
    @ApiModelProperty(value = "代码语言")
    private String language;
    @ApiModelProperty(value = "代码长度")
    private Integer codeLength;
    @ApiModelProperty(value = "提交人用户ID")
    private Long creatorId;
    @ApiModelProperty(value = "提交人用户名")
    private String creator;
}

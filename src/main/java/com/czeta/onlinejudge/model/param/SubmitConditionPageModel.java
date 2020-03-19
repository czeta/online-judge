package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SubmitConditionPageModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:55
 * @Version 1.0
 */
@ApiModel(description = "提交评测数据的筛选参数与分页参数")
@Data
public class SubmitConditionPageModel {
    @ApiModelProperty(value = "分页model")
    private PageModel pageModel;

    @ApiModelProperty(value = "筛选参数：提交人用户名关键字")
    private String creatorKey;
    @ApiModelProperty(value = "筛选参数：问题ID")
    private Long problemId;
    @ApiModelProperty(value = "筛选参数：提交状态：Accepted等...")
    private String submitStatus;
}

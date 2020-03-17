package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProblemTagModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/14 11:22
 * @Version 1.0
 */
@ApiModel(description = "题目标签model")
@Data
public class ProblemTagModel {
    @ApiModelProperty(value = "题目ID")
    Long problemId;
    @ApiModelProperty(value = "标签ID")
    Integer tagId;
    @ApiModelProperty(value = "标签名")
    String name;
}

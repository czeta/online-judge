package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SpiderProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 17:42
 * @Version 1.0
 */
@ApiModel(description = "爬虫评测的题目model")
@Data
public class SpiderProblemModel {
    @ApiModelProperty(value = "题目标题，为空时则使用目标OJ的题目标题")
    private String title;

    @ApiModelProperty(value = "评测方式ID")
    private Integer judgeTypeId;

    @ApiModelProperty(value = "目标OJ的题目ID")
    private Integer spiderProblemId;
}

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
    @ApiModelProperty(value = "题目标题（如果为空，则使用目标OJ的题目的标题")
    private String title;

    @ApiModelProperty(value = "评测方式ID")
    private Integer judgeTypeId;
    @ApiModelProperty(value = "题目类型：0表示ACM/ICPC题型、1表示函数型题型")
    private Integer problemType;
    @ApiModelProperty(value = "目标OJ的题目ID")
    private Integer spiderProblemId;
}

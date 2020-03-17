package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SimpleProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/15 17:20
 * @Version 1.0
 */
@ApiModel(description = "简单信息题目model")
@Data
public class SimpleProblemModel {
    @ApiModelProperty(value = "题目ID")
    private Long id;
    @ApiModelProperty(value = "题目标题")
    private String title;
    @ApiModelProperty(value = "题目创建者")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private String crtTs;
    @ApiModelProperty(value = "题目状态：1表示正常并可视，0表示正常但不可视（这部分是比赛题目才有的），-1表示题目禁用")
    private Short status;
}

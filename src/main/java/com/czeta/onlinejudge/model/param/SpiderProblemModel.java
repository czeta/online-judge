package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @ApiModelProperty(value = "评测方式ID")
    private Integer judgeTypeId;
    @ApiModelProperty(value = "目标OJ的题目ID")
    private Long spiderProblemId;

    @ApiModelProperty(value = "题目标题，为空时则使用目标OJ的题目标题")
    private String title;
    @ApiModelProperty(value = "题目难度：支持easy、medium、hard")
    private String level;
    @ApiModelProperty(value = "题目所属标签ID列表")
    private List<Integer> tagId;

    @ApiModelProperty(value = "来源ID：如果是比赛下的题目，那么这里置为比赛ID；如果不是则为0")
    private Long sourceId;
    @ApiModelProperty(value = "来源名称：如果是比赛下的题目，那么这里置为比赛标题；如果不是则可任意值，任意值必须以@开头")
    private String sourceName;

    @ApiModelProperty(value = "题目状态：1表示正常并可视，0表示正常但不可视（即只能在比赛界面看到该题，不能在题目模块看到该题），-1表示题目禁用")
    private Short status;
}

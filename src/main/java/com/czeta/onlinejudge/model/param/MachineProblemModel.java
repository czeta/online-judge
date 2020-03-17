package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MachineProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 19:52
 * @Version 1.0
 */
@ApiModel(description = "评测机评测的题目model")
@Data
public class MachineProblemModel {
    @ApiModelProperty(value = "题目ID：如果是创建的操作，这部分置为null；如果是更新的操作这部分必须有")
    private Long id;

    @ApiModelProperty(value = "题目标题")
    private String title;
    @ApiModelProperty(value = "题目描述")
    private String description;
    @ApiModelProperty(value = "输入描述")
    private String inputDescription;
    @ApiModelProperty(value = "输出描述")
    private String outputDescription;
    @ApiModelProperty(value = "输入样例")
    private String inputSamples;
    @ApiModelProperty(value = "输出样例")
    private String outputSamples;
    @ApiModelProperty(value = "提示")
    private String hint;
    @ApiModelProperty(value = "来源名称：如果是比赛下的题目，那么这里置为比赛标题；如果不是则可任意值，任意值必须以@开头")
    private String sourceName;
    @ApiModelProperty(value = "时间限制，数字表示，默认以ms为单位")
    private Integer timeLimit;
    @ApiModelProperty(value = "内存限制，数字表示，默认以mb为单位")
    private Integer memoryLimit;
    @ApiModelProperty(value = "IO模型：只支持 Standard IO")
    private String ioMode;
    @ApiModelProperty(value = "题目难度：支持easy、medium、hard")
    private String level;
    @ApiModelProperty(value = "语言：C,C++,Java。如果支持多种语言，则每个语言之间以逗号分隔，只接收字符串")
    private String language;
    @ApiModelProperty(value = "题目状态：1表示正常并可视，0表示正常但不可视（即只能在比赛界面看到该题，不能在题目模块看到该题），-1表示题目禁用")
    private Short status;

    @ApiModelProperty(value = "题目评测方式ID")
    private Integer judgeTypeId;
    @ApiModelProperty(value = "题目类型：0表示ACM/ICPC题型、1表示函数型题型")
    private Integer problemType;
    @ApiModelProperty(value = "是否特判（针对题目评测方式ID为评测机评测），1表示特判，0表示不是")
    private Integer spj;

    @ApiModelProperty(value = "题目所属标签ID列表")
    private List<Integer> tagId;
}

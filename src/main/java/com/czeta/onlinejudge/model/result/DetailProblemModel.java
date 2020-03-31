package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DetailProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 14:32
 * @Version 1.0
 */
@ApiModel(description = "具体比赛详情model")
@Data
public class DetailProblemModel {
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
    @ApiModelProperty(value = "来源ID：如果是比赛下的题目，那么这里置为比赛ID；如果不是则为0")
    private Long sourceId;
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
    @ApiModelProperty(value = "语言：C,C++,Java。如果支持多种语言，则每个语言之间以逗号分隔")
    private String language;
    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "提交次数")
    private Integer submitCount;
    @ApiModelProperty(value = "通过次数")
    private Integer acCount;
    @ApiModelProperty(value = "提交人数")
    private Integer submitNum;
    @ApiModelProperty(value = "通过人数")
    private Integer acNum;

    @ApiModelProperty(value = "统计数据：key为提交状态，value为状态数量。" +
            "key有：Accepted、WrongAnswer、TimeLimitExceeded、OutputLimitExceeded、MemoryLimitExceeded、" +
            "RuntimeError、PresentationError、CompilationError、Pending、SystemError")
    private Map<String, Integer> statistic;

    @ApiModelProperty(value = "评测类型名")
    private String judgeTypeName;
    @ApiModelProperty(value = "题目类型：0表示ACM/ICPC题型、1表示函数型题型")
    private Integer problemType;
    @ApiModelProperty(value = "代码模板（针对题目为函数型题型）")
    private String codeTemplate;
    @ApiModelProperty(value = "是否特判（针对题目评测方式ID为评测机评测），1表示特判，0表示不是")
    private Integer spj;

    @ApiModelProperty(value = "问题的标签列表")
    private List<ProblemTagModel> tagList;


}

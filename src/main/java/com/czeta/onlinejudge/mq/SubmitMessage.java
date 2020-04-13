package com.czeta.onlinejudge.mq;

import com.czeta.onlinejudge.dao.entity.JudgeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @ClassName SubmitMessage
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/29 17:36
 * @Version 1.0
 */
@ApiModel(description = "提交MQ的消息格式")
@Data
public class SubmitMessage {
    // 提交的评测ID
    @ApiModelProperty(value = "提交的评测ID")
    Long submitId;

    // 用户提交信息
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户提交代码(base64编码)")
    private String code;
    @ApiModelProperty(value = "用户提交代码的语言")
    private String language;

    // 提交的题目信息
    @ApiModelProperty(value = "题目ID")
    Long problemId;
    @ApiModelProperty(value = "题目来源ID：0为无来源，大于0为来源比赛的ID")
    private Long sourceId;
    @ApiModelProperty(value = "时间限制(ms)")
    private Integer timeLimit;
    @ApiModelProperty(value = "内存限制(MB)")
    private Integer memoryLimit;

    // 提交的题目评测方式信息
    @ApiModelProperty(value = "评测类型：0代表爬虫、1代表评测机")
    private Short judgeType;
    @ApiModelProperty(value = "评测名称：爬虫名称或评测机名称")
    private String judgeName;
    @ApiModelProperty(value = "爬虫目标url或评测机服务所在url")
    private String judgeUrl;
    @ApiModelProperty(value = "同名的评测方式列表")
    private List<JudgeType> judgeTypeList;
    @ApiModelProperty(value = "评测机访问token")
    private String visitToken;
    @ApiModelProperty(value = "是否特判，1表示特判，0表示不是")
    private Integer spj;
    @ApiModelProperty(value = "特判代码(base64编码)")
    private String spjCode;
    @ApiModelProperty(value = "特判代码的语言")
    private String spjLanguage;
    @ApiModelProperty(value = "特判版本")
    private String spjVersion;
    @ApiModelProperty(value = "表示目标OJ的ID")
    private Long spiderProblemId;

}
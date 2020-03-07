package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName JudgeTypeModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 20:15
 * @Version 1.0
 */
@ApiModel(description = "评测类型model")
@Data
public class JudgeTypeModel {
    @ApiModelProperty(value = "评测类型ID，如果是添加的功能，则这部分置为null；如果是更新的功能，则不能为null")
    private Integer id;
    @ApiModelProperty(value = "评测名称：爬虫名称或评测机名称")
    private String name;
    @ApiModelProperty(value = "爬虫目标url或评测机服务所在url")
    private String url;
    @ApiModelProperty(value = "爬虫和评测机状态：0表示被停用，1表示正常，-1表示异常")
    private Short status;
    // 评测机专有属性：
    @ApiModelProperty(value = "主机名")
    private String hostname;
}

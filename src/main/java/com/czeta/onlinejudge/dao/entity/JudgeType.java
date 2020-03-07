package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName JudgeType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:24
 * @Version 1.0
 */
@ApiModel(description = "评测类型")
@Data
public class JudgeType {
    @ApiModelProperty(value = "评测类型ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "评测类型：0代表爬虫、1代表评测机")
    private Short type;
    @ApiModelProperty(value = "评测名称：爬虫名称或评测机名称")
    private String name;
    @ApiModelProperty(value = "爬虫目标url或评测机服务所在url")
    private String url;
    @ApiModelProperty(value = "爬虫和评测机状态：0表示被停用，1表示正常，-1表示异常")
    private Short status;

    // 评测机专有属性：
    @ApiModelProperty(value = "主机名")
    private String hostname;
    @ApiModelProperty(value = "cpu核数")
    private Short cpuCore;
    @ApiModelProperty(value = "cpu使用率")
    private String cpuUsage;
    @ApiModelProperty(value = "内存使用率")
    private String memoryUsage;
    @ApiModelProperty(value = "最后一次心跳上报的时间")
    private String lastHeartBeat;

    @ApiModelProperty(value = "创建时间")
    private String crtTs;
    @ApiModelProperty(value = "最后一次信息修改时间")
    private String lmTs;
}

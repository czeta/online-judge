package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName JudgeType
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:24
 * @Version 1.0
 */
@Data
public class JudgeType {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 0代表爬虫、1代表评测机
    private Short type;
    // 爬虫名称或评测机名称
    private String name;
    // 爬虫目标url或评测机服务所在url
    private String url;
    // 爬虫和评测机状态，0表示被停用，1表示正常，-1表示异常
    private Short status;

    // 评测机专有属性：
    // 主机名
    private String hostname;
    // cpu核数
    private Short cpuCore;
    // cpu使用率
    private String cpuUsage;
    // 内存使用率
    private String memoryUsage;
    // 最后一次心跳上报的时间
    private String lastHeartBeat;

    private String crtTs;
    private String lmTs;
}

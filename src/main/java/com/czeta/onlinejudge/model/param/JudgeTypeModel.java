package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName JudgeTypeModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 20:15
 * @Version 1.0
 */
@Data
public class JudgeTypeModel {
    private Integer id;
    // 爬虫名称或评测机名称
    private String name;
    // 爬虫目标url或评测机服务所在url
    private String url;
    // 爬虫和评测机状态，0表示被停用，1表示正常，-1表示异常
    private Short status;
    // 评测机专有属性：
    // 主机名
    private String hostname;
}

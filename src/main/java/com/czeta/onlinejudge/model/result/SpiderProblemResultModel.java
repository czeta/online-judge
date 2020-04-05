package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName SpiderProblemResultModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/5 15:41
 * @Version 1.0
 */
@Data
public class SpiderProblemResultModel {
    private Long spiderProblemId;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String inputSamples;
    private String outputSamples;
    private String hint;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String language;
}

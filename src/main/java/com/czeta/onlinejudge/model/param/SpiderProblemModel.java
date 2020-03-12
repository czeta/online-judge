package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName SpiderProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 17:42
 * @Version 1.0
 */
@Data
public class SpiderProblemModel {
    private String title;

    private Integer judgeTypeId;
    private Integer problemType;
    private Integer spiderProblemId;
}

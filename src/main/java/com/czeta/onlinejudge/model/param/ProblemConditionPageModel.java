package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName ProblemConditionModel
 * @Description 问题筛选条件
 * @Author chenlongjie
 * @Date 2020/3/17 15:01
 * @Version 1.0
 */
@Data
public class ProblemConditionPageModel {
    private PageModel pageModel;

    private String titleKey;
    private String level;
    private Integer tagId;
}

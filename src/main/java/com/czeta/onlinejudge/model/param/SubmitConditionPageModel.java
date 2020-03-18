package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName SubmitConditionPageModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:55
 * @Version 1.0
 */
@Data
public class SubmitConditionPageModel {
    private PageModel pageModel;

    private String creatorKey;
    private Long problemId;
    private String submitStatus;
}

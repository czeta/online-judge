package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName SubmitResultModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/22 10:38
 * @Version 1.0
 */
@Data
public class SubmitResultModel {
    private Long submitId;
    private Long problemId;
    private String time;
    private String memory;
    private String submitStatus;
}

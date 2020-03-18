package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName SubmitModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 16:16
 * @Version 1.0
 */
@Data
public class SubmitModel {
    private Long problemId;
    private String code;
    private String language;
}

package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName PublicSubmitModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/18 11:20
 * @Version 1.0
 */
@Data
public class PublicSubmitModel {
    private Long id;
    private String crtTs;
    private String submitStatus;
    private Long problemId;
    private String time;
    private String memory;
    private String language;
    private Integer codeLength;
    private Long creatorId;
    private String creator;
}

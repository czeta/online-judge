package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName SimpleProblemModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/15 17:20
 * @Version 1.0
 */
@Data
public class SimpleProblemModel {
    private Long id;
    private String title;
    private String creator;
    private String crtTs;
    private Short status;
}

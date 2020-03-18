package com.czeta.onlinejudge.model.result;

import lombok.Data;

/**
 * @ClassName PublicSimpleProblemMode
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/17 13:52
 * @Version 1.0
 */
@Data
public class PublicSimpleProblemModel {
    private Long id;
    private String title;
    private String level;
    private Integer submitCount;
    private String acRate;
}

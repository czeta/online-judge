package com.czeta.onlinejudge.model.result;


import lombok.Data;

/**
 * @ClassName PublicRankModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/20 10:51
 * @Version 1.0
 */
@Data
public class PublicRankModel {
    private String username;
    private String mood;
    private Integer submitCount;
    private Integer acNum;
    private Integer ratingScore;
}

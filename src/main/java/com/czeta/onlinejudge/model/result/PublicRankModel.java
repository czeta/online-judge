package com.czeta.onlinejudge.model.result;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PublicRankModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/20 10:51
 * @Version 1.0
 */
@ApiModel(description = "rank榜单 model")
@Data
public class PublicRankModel {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "心情")
    private String mood;
    @ApiModelProperty(value = "提交题目的次数")
    private Integer submitCount;
    @ApiModelProperty(value = "通过题目的数量")
    private Integer acNum;
    @ApiModelProperty(value = "积分")
    private Integer ratingScore;
}

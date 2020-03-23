package com.czeta.onlinejudge.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PublicSimpleProblemMode
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/17 13:52
 * @Version 1.0
 */
@ApiModel(description = "公有简单问题信息model")
@Data
public class PublicSimpleProblemModel {
    @ApiModelProperty(value = "问题ID")
    private Long id;
    @ApiModelProperty(value = "问题标题")
    private String title;
    @ApiModelProperty(value = "问题难度")
    private String level;
    @ApiModelProperty(value = "提交数量")
    private Integer submitCount;
    @ApiModelProperty(value = "AC通过率")
    private String acRate;
}

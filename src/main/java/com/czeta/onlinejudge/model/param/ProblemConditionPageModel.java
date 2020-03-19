package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProblemConditionModel
 * @Description 分页参数和筛选参数
 * @Author chenlongjie
 * @Date 2020/3/17 15:01
 * @Version 1.0
 */
@ApiModel(description = "分页参数和筛选参数 model")
@Data
public class ProblemConditionPageModel {
    @ApiModelProperty(value = "分页参数")
    private PageModel pageModel;

    @ApiModelProperty(value = "筛选参数：问题标题关键字")
    private String titleKey;
    @ApiModelProperty(value = "筛选参数：问题难度水平")
    private String level;
    @ApiModelProperty(value = "筛选参数：问题的某个标签ID")
    private Integer tagId;
}

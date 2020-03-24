package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ContestConditionPageModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/19 12:29
 * @Version 1.0
 */
@ApiModel(description = "分页参数和筛选参数 model")
@Data
public class ContestConditionPageModel {
    @ApiModelProperty(value = "分页参数")
    private PageModel pageModel;

    @ApiModelProperty(value = "比赛标题关键字")
    private String titleKey;
    @ApiModelProperty(value = "报名规则：公开、认证、密码")
    private String signUpRule;
    @ApiModelProperty(value = "排名模式：练习、积分、ACM/ICPC")
    private String rankModel;
    @ApiModelProperty(value = "比赛进行状态：未开始、进行中、已结束")
    private String contestRunningStatus;
}
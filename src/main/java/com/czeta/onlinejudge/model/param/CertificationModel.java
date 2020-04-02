package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CertificationModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 12:08
 * @Version 1.0
 */
@ApiModel(description = "实名认证类型Model")
@Data
public class CertificationModel {
    @ApiModelProperty("认证类型ID，如果是添加的功能，则这部分置为null；如果是更新的功能，则不能为null")
    Integer id;
    @ApiModelProperty("认证名")
    String name;
}

package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName certification
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 12:57
 * @Version 1.0
 */
@ApiModel(description = "实名认证类型")
@Data
public class Certification {
    @ApiModelProperty(value = "类型ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "类型名")
    private String name;
    @ApiModelProperty(value = "状态：0表示弃用、1表示正在使用")
    private Short status;
    @ApiModelProperty(value = "类型创建时间")
    private String crtTs;
    @ApiModelProperty(value = "类型最后修改时间")
    private String lmTs;
}

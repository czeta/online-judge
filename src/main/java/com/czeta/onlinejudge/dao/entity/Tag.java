package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Tag
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:28
 * @Version 1.0
 */
@ApiModel(description = "题目标签实体")
@Data
public class Tag {
    @ApiModelProperty(value = "标签ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "标签名")
    private String name;
    @ApiModelProperty(value = "创建者")
    private String creator;
    @ApiModelProperty(value = "状态，1表示启用，0表示弃用")
    private Short status;
    @ApiModelProperty(value = "创建时间")
    private String crtTs;
    @ApiModelProperty(value = "标签最后修改时间")
    private String lmTs;
}

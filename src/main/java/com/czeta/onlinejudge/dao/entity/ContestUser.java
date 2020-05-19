package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ContestUser
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:33
 * @Version 1.0
 */
@ApiModel(description = "报名比赛的用户model")
@Data
public class ContestUser {
    @ApiModelProperty(value = "Model的ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "比赛ID")
    private Long contestId;
    @ApiModelProperty(value = "报名的用户ID")
    private Long userId;
    @ApiModelProperty(value = "报名状态：0表示尚未审核、1表示审核通过、-1表示审核不通过")
    private Short status;
    @ApiModelProperty(value = "报名时间")
    private String crtTs;
    @ApiModelProperty(value = "最后修改时间")
    private String lmTs;
    @ApiModelProperty(value = "额外信息字段：这里是报名的用户的认证信息，序列化后")
    @TableField(exist = false)
    private String detailMsg;
}

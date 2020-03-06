package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Message
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:04
 * @Version 1.0
 */
@ApiModel(description = "消息实体")
@Data
public class Message {
    @ApiModelProperty(value = "消息ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "消息标题")
    private String title;
    @ApiModelProperty(value = "消息实体")
    private String content;
    @ApiModelProperty(value = "消息发送者名称")
    private String creator;
    @ApiModelProperty(value = "消息接收者ID")
    private Long userId;
    @ApiModelProperty(value = "消息状态：0为未读，1为已读")
    private Short status;
    @ApiModelProperty(value = "消息创建时间")
    private String crtTs;
    @ApiModelProperty(value = "消息最后修改时间")
    private String lmTs;
}

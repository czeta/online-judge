package com.czeta.onlinejudge.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Announcement
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:06
 * @Version 1.0
 */
@ApiModel(description = "公告")
@Data
public class Announcement {
    @ApiModelProperty(value = "公告ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "公告标题")
    private String title;
    @ApiModelProperty(value = "公告内容")
    private String content;
    @ApiModelProperty(value = "公告创建者")
    private String creator;
    @ApiModelProperty(value = "公告类型（来源）：-1表示主页公告、0表示FAQ、大于0表示竞赛的公告，数字代表竞赛ID")
    private Long sourceId;
    @ApiModelProperty(value = "公告状态：1表示启用、0表示弃用")
    private Short status;
    @ApiModelProperty(value = "创建时间")
    private String crtTs;
    @ApiModelProperty(value = "公告最后修改时间")
    private String lmTs;
}

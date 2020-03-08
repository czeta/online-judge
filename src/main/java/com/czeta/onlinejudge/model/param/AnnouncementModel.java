package com.czeta.onlinejudge.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Announcement
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 14:30
 * @Version 1.0
 */
@ApiModel(description = "公告model")
@Data
public class AnnouncementModel {
    @ApiModelProperty(value = "公告ID，如果是添加的功能，这部分置为null；如果是修改的功能，这部分不能为null")
    private Long id;
    @ApiModelProperty(value = "公告标题")
    private String title;
    @ApiModelProperty(value = "公告内容")
    private String content;
    @ApiModelProperty(value = "公告状态：1表示启用，0表示弃用")
    private Short status;
}

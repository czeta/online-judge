package com.czeta.onlinejudge.model.param;

import lombok.Data;

/**
 * @ClassName Announcement
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/4 14:30
 * @Version 1.0
 */
@Data
public class AnnouncementModel {
    private Long id;
    private String title;
    private String content;
    private Short status;
}

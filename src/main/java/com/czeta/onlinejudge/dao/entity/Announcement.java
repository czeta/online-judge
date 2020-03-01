package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName Announcement
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:06
 * @Version 1.0
 */
@Data
public class Announcement {
    private Long id;
    private String title;
    private String content;
    private String creator;
    private Long sourceId;
    private Short status;
    private String crtTs;
    private String lmTs;
}

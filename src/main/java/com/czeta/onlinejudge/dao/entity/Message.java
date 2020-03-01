package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName Message
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:04
 * @Version 1.0
 */
@Data
public class Message {
    private Long id;
    private String title;
    private String content;
    private String creator;
    private Long userId;
    private Short status;
    private String crtTs;
    private String lmTs;
}

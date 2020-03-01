package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName Tag
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:28
 * @Version 1.0
 */
@Data
public class Tag {
    private Integer id;
    private String name;
    private String creator;
    private Short status;
    private String crtTs;
    private String lmTs;
}

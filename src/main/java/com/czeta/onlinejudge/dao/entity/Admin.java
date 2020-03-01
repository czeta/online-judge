package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName Admin
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:01
 * @Version 1.0
 */
@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String description;
    private Integer roleId;
    private Short status;
    private String crtTs;
    private String lmTs;
}

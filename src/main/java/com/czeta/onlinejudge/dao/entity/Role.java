package com.czeta.onlinejudge.dao.entity;

import lombok.Data;

/**
 * @ClassName Role
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 13:03
 * @Version 1.0
 */
@Data
public class Role {
    private Integer id;
    private String name;
    private String permissionCodes;
    private Short status;
    private String crtTs;
    private String lmTs;
}

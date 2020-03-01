package com.czeta.onlinejudge.enums;


/**
 * @ClassName RoleEnum
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 15:28
 * @Version 1.0
 */
public enum RoleType  {
    COMMON(1, "Common"),
    ADMIN(2, "Admin"),
    SUPER_ADMIN(3, "SuperAdmin");

    private Integer id;
    private String name;

    private RoleType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

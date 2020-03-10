package com.czeta.onlinejudge.enums;


import com.czeta.onlinejudge.utils.enums.IEnumItem;

/**
 * @ClassName RoleEnum
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/1 15:28
 * @Version 1.0
 */
public enum RoleType implements IEnumItem {
    COMMON_USER(1, Names.COMMON_USER),
    COMMON_ADMIN(2, Names.COMMON_ADMIN),
    SUPER_ADMIN(3, Names.SUPER_ADMIN);

    private Integer code;
    private String message;

    public class Names {
        public static final String COMMON_USER = "CommonUser";
        public static final String COMMON_ADMIN = "CommonAdmin";
        public static final String SUPER_ADMIN = "SuperAdmin";
    }

    private RoleType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

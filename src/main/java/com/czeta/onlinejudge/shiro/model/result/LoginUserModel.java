package com.czeta.onlinejudge.shiro.model.result;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;


/**
 * @ClassName LoginUserModel
 * @Description 登录用户信息model
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@Data
@Accessors(chain = true)
public class LoginUserModel {

    private Long id;

    private String username;

    private Long roleId;

    private String roleName;

    private Set<String> permissionCodes;

    private Integer status;

    public void setPermissionCodes(Set<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }

    public void setPermissionCodesFromString(String permissionString) {
        Set<String> set = new HashSet<>();
        try {
            String[] permissions= permissionString.split(",");
            for (String str : permissions) {
                set.add(str);
            }
            this.permissionCodes = set;
        } catch (Exception e) {
            log.error("权限格式有误");
        }
    }
}

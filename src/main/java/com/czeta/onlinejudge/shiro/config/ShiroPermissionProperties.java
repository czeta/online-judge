package com.czeta.onlinejudge.shiro.config;

import lombok.Data;

/**
 * @ClassName ShiroPermissionProperties
 * @Description Shiro权限配置映射类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Data
public class ShiroPermissionProperties {

    private String url;

    private String[] urls;

    private String permission;

}

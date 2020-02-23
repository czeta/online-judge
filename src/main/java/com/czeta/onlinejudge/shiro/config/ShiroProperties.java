package com.czeta.onlinejudge.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ShiroProperties
 * @Description Shiro配置映射类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private boolean enable;

    @NestedConfigurationProperty
    private List<ShiroPermissionProperties> permission;

}

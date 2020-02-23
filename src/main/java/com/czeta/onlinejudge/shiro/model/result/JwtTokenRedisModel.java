package com.czeta.onlinejudge.shiro.model.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @ClassName JwtTokenRedisModel
 * @Description JwtToken Redis缓存model
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class JwtTokenRedisModel{
    /**
     * 登陆ip
     */
    private String host;
    /**
     * 登陆用户名称
     */
    private String username;
    /**
     * 登陆盐值
     */
    private String salt;
    /**
     * 登陆token
     */
    private String token;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;
    /**
     * 过期日期
     */
    private Date expireDate;
}
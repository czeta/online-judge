package com.czeta.onlinejudge.shiro.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.czeta.onlinejudge.shiro.util.JwtTokenUtil;
import com.czeta.onlinejudge.util.utils.IpUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.shiro.authc.HostAuthenticationToken;

import java.util.Date;


/**
 * @ClassName JwtToken
 * @Description JwtToken对象
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class JwtToken implements HostAuthenticationToken {
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
     * 过期日期
     */
    private Date expireDate;
    /**
     * 多长时间过期，默认一小时
     */
    private long expireSecond;

    /**
     * 这里是token
     */
    private String principal;

    /**
     * 这里是token
     */
    private String credentials;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    /**
     * 实例化jwttoken对象
     * @param token
     * @param username
     * @param salt
     * @param expireSecond
     * @return
     */
    public static JwtToken build(String token, String username, String salt, long expireSecond) {
        DecodedJWT decodedJWT = JwtTokenUtil.getJwtInfo(token);
        Date createDate = decodedJWT.getIssuedAt();
        Date expireDate = decodedJWT.getExpiresAt();
        return new JwtToken()
                .setHost(IpUtils.getRequestIp())
                .setUsername(username)
                .setSalt(salt)
                .setToken(token)
                .setCreateDate(createDate)
                .setExpireDate(expireDate)
                .setExpireSecond(expireSecond);
    }

}

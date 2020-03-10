package com.czeta.onlinejudge.shiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.czeta.onlinejudge.shiro.consts.CommonConstant;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.utils.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName JwtTokenUtil
 * @Description JwtToken的工具类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@Component
public class JwtTokenUtil {
    private static JwtProperties jwtProperties;

    public JwtTokenUtil(JwtProperties jwtProperties) {
        JwtTokenUtil.jwtProperties = jwtProperties;
    }

    /**
     * 生成JWT Token串
     * @param username
     * @param salt
     * @return
     */
    public static String generateToken(String username, String salt) {
        try {
            if (StringUtils.isBlank(username)) {
                log.error("username不能为空");
                return null;
            }
            // 如果盐值为空，则使用secret密钥值
            if (StringUtils.isBlank(salt)) {
                salt = jwtProperties.getSecret();
            }
            Long expireSecond = jwtProperties.getExpireSecond();
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim(CommonConstant.JWT_USERNAME, username)
                    .withJWTId(UUIDUtils.getUUID())              // jwt唯一id
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .withIssuedAt(new Date())                   // 签名时间
                    .withExpiresAt(expireDate)                  // token过期时间
                    .sign(algorithm);                           // 签名
            return token;
        } catch (Exception e) {
            log.error("generateToken exception", e);
        }
        return null;
    }

    /**
     * 验证jwt token是否有效
     * @param token
     * @param salt
     * @return
     */
    public static boolean verifyToken(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 获取token的payload中的自定义属性-用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        String username = decodedJWT.getClaim(CommonConstant.JWT_USERNAME).asString();
        return username;
    }

    /**
     * 获取token的payload中的自定义属性-ID
     * @param token
     * @return
     */
    public static Long getUserId(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        Long userId = decodedJWT.getClaim(CommonConstant.JWT_USER_ID).asLong();
        return userId;
    }


    /**
     * 判断token是否已过期
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }

    /**
     * 获取token过期时间
     * @param token
     * @return
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        return decodedJWT.getExpiresAt();
    }

    /**
     * 解析token，获取token数据
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT;
    }
}

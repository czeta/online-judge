package com.czeta.onlinejudge.shiro.util;

import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;


/**
 * @ClassName SaltUtil
 * @Description 盐值包装工具类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class SaltUtil {

    /**
     * 盐值包装
     * @param secret
     * @param salt
     * @return
     */
    public static String getSalt(String secret, String salt) {
        if (StringUtils.isBlank(secret) && StringUtils.isBlank(salt)) {
            return null;
        }
        // 加密方法
        String newSalt = DigestUtils.sha256Hex(secret + salt);
        return newSalt;
    }

    /**
     * 生成32位随机盐
     * @return
     */
    public static String generateSalt() {
        return new SecureRandomNumberGenerator().nextBytes(16).toHex();
    }

    /**
     * 加工盐值
     * @param salt
     * @param jwtProperties
     * @return
     */
    public static String getSalt(String salt, JwtProperties jwtProperties) {
        String newSalt;
        if (jwtProperties.isSaltCheck()) {
            // 包装盐值
            newSalt = SaltUtil.getSalt(jwtProperties.getSecret(), salt);
        } else {
            newSalt = jwtProperties.getSecret();
        }
        return newSalt;
    }

}


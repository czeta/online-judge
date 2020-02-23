package com.czeta.onlinejudge.util.utils;

import com.czeta.onlinejudge.util.enums.IBaseStatusMsg;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName PasswordUtils
 * @Description 密码工具类
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class PasswordUtils {

    /**
     * 密码加盐，再加密
     * @param pwd
     * @param salt
     * @return
     */
    public static String encrypt(String pwd, String salt) {
        AssertUtils.notBlank(pwd, IBaseStatusMsg.APIEnum.PARAM_ERROR, "密码不能为空");
        AssertUtils.notBlank(salt, IBaseStatusMsg.APIEnum.PARAM_ERROR, "盐值不能为空");
        return DigestUtils.sha256Hex(pwd + salt);
    }
}

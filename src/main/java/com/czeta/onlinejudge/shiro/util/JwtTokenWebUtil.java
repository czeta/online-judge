package com.czeta.onlinejudge.shiro.util;

import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName JwtTokenWebUtil
 * @Description JwtToken的web工具类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@Component
public class JwtTokenWebUtil {
    private static String tokenName;

    public JwtTokenWebUtil(JwtProperties jwtProperties) {
        tokenName = jwtProperties.getTokenName();
    }

    /**
     * 获取token名称
     * @return
     */
    public static String getTokenName() {
        return tokenName;
    }

    /**
     * 从请求头/参数中取出token
     * @return
     */
    public static String getTokenFromRequestHeader() {
        return getTokenFromRequestHeader(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    /**
     * 从请求头/参数中取出token
     * @param request
     * @return
     */
    public static String getTokenFromRequestHeader(HttpServletRequest request) {
        AssertUtils.notNull(request, IBaseStatusMsg.APIEnum.PARAM_ERROR);
        String token = request.getHeader(tokenName);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(tokenName);
        }
        return token;
    }
}

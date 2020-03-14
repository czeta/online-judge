package com.czeta.onlinejudge.shiro.jwt;

import com.czeta.onlinejudge.shiro.cache.LoginRedisService;
import com.czeta.onlinejudge.shiro.enums.ShiroStatusMsg;
import com.czeta.onlinejudge.shiro.service.LoginService;
import com.czeta.onlinejudge.shiro.util.JwtTokenUtil;
import com.czeta.onlinejudge.shiro.util.JwtTokenWebUtil;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName JwtFilter
 * @Description JWT过滤器
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    private JwtProperties jwtProperties;

    private LoginRedisService loginRedisService;

    private LoginService loginService;

    public JwtFilter(JwtProperties jwtProperties, LoginRedisService loginRedisService, LoginService loginService) {
        this.jwtProperties = jwtProperties;
        this.loginRedisService = loginRedisService;
        this.loginService = loginService;
    }

    /**
     * 判断是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        log.info("请求URL={}; 请求类型={}", url, method);
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (APIRuntimeException e) { // 捕获异常后，返回false，并转到onAccessDenied方法，封装API结果返回
            log.error("code={} messag={}", e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("访问错误", e);
        }
        return allowed;
    }

    /**
     * 将JWT Token包装成AuthenticationToken，再执行login
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String token = JwtTokenWebUtil.getTokenFromRequestHeader();
        AssertUtils.notBlank(token, ShiroStatusMsg.INVALID_TOKEN, "JWT Token为空");
        if (JwtTokenUtil.isExpired(token)) {
            throw new APIRuntimeException(ShiroStatusMsg.INVALID_TOKEN, "JWT Token过期");
        }
        // 如果开启redis二次校验，或者设置为单个用户token登陆，则先在redis中判断token是否存在
        if (jwtProperties.isRedisCheck()) {
            boolean redisExpired = loginRedisService.exists(token);
            AssertUtils.isTrue(redisExpired, ShiroStatusMsg.INVALID_TOKEN, "Redis Token不存在,token:" + token);
        }
        String username = JwtTokenUtil.getUsername(token);
        String salt;
        if (jwtProperties.isSaltCheck()) {
            salt = loginRedisService.getSalt(username);
        } else {
            salt = jwtProperties.getSecret();
        }
        log.info("JwtFilter createToken token={}", token);
        // 添加到attribute中，作为controller的参数传入，避免再次解析token带来的不优雅
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Long userId = loginRedisService.getUserId(username);
        request.setAttribute("userId", userId);
        log.info("RequestAttribute userId={}", userId);
        return JwtToken.build(token, username, salt, jwtProperties.getExpireSecond());
    }

    /**
     * 访问失败处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(200);
        // 封装成自定义API（header仍然返回200正确状态码，但是自定义status返回登录受限的状态码）
        HttpUtils.printJSON(httpServletResponse, new APIResult<>(ShiroStatusMsg.LOGIN_AUTHORITY_EXCEED));
        log.info("login fail, no authority");
        return false;
    }

    /**
     * 登陆成功处理
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        // 刷新token
        JwtToken jwtToken = (JwtToken) token;
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        loginService.refreshToken(jwtToken, httpServletResponse);
        log.info("login success");
        return true;
    }

    /**
     * 登陆失败处理
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.info("login fail");
        return false;
    }
}

package com.czeta.onlinejudge.shiro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czeta.onlinejudge.dao.entity.Role;
import com.czeta.onlinejudge.dao.entity.User;
import com.czeta.onlinejudge.dao.mapper.RoleMapper;
import com.czeta.onlinejudge.dao.mapper.UserMapper;
import com.czeta.onlinejudge.shiro.cache.LoginRedisService;
import com.czeta.onlinejudge.shiro.convert.ShiroMapstructConvert;
import com.czeta.onlinejudge.shiro.enums.ShiroStatusMsg;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.shiro.jwt.JwtToken;
import com.czeta.onlinejudge.shiro.model.param.LoginParamModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserModel;
import com.czeta.onlinejudge.shiro.service.LoginService;
import com.czeta.onlinejudge.shiro.util.JwtTokenUtil;
import com.czeta.onlinejudge.shiro.util.JwtTokenWebUtil;
import com.czeta.onlinejudge.shiro.util.SaltUtil;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import com.czeta.onlinejudge.util.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * @ClassName LoginServiceImpl
 * @Description 登录服务实现类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRedisService loginRedisService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public String login(LoginParamModel loginParamModel) {
        String username = loginParamModel.getUsername();
        // 从数据库中获取登陆用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("status", 1));
        AssertUtils.notNull(user, ShiroStatusMsg.PARAM_ERROR,"用户名或密码错误");
        // 原始密码明文：123456，原始密码前端加密：sha256(123456)。后台加密规则：sha256(sha256(123456) + salt)。
        // 这里使用的是jwt配置的secret（与token加的盐一致，前提是saltCheck为false）
        String encryptPassword = PasswordUtils.encrypt(loginParamModel.getPassword(), jwtProperties.getSecret());
        AssertUtils.equal(encryptPassword, user.getPassword(), ShiroStatusMsg.PARAM_ERROR, "用户名或密码错误");
        // 将系统用户对象转换成登陆用户对象
        LoginUserModel loginUserModel = ShiroMapstructConvert.INSTANCE.userToLoginUserModel(user);
        // 获取当前用户角色
        Role role = roleMapper.selectById(user.getRoleId());
        AssertUtils.notNull(role, ShiroStatusMsg.PARAM_ERROR, "角色不存在");
        AssertUtils.notBlank(role.getPermissionCodes(), ShiroStatusMsg.PARAM_ERROR, "权限为空");
        loginUserModel.setRoleName(role.getName()).setPermissionCodesFromString(role.getPermissionCodes());
        log.info("LoginServiceImpl login loginUserModel={}", loginUserModel);

        String salt = SaltUtil.getSalt(jwtProperties.getSecret(), jwtProperties);
        String token = JwtTokenUtil.generateToken(username, salt);
        // 创建AuthenticationToken，登录
        JwtToken jwtToken = JwtToken.build(token, username, salt, jwtProperties.getExpireSecond());
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);
        // 缓存登陆信息到Redis
        loginRedisService.cacheLoginInfo(jwtToken, loginUserModel);
        log.info("LoginServiceImpl login token={}", token);
        return token;
    }

    @Override
    public void logout(HttpServletRequest request) {
        // 退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String token = JwtTokenWebUtil.getTokenFromRequestHeader(request);
        String username = JwtTokenUtil.getUsername(token);
        log.info("LoginServiceImpl logout token={} username={}", token, username);
        // 删除Redis缓存信息
        loginRedisService.deleteUserLoginInfo(token, username);
    }

    @Override
    public void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse) {
        if (jwtToken == null) {
            return;
        }
        if (jwtToken == null || StringUtils.isBlank(jwtToken.getToken())) {
            return;
        }
        // 判断是否刷新token
        boolean isRefreshToken = jwtProperties.isRefreshToken();
        if (!isRefreshToken) {
            return;
        }
        String token = jwtToken.getToken();
        // 获取过期时间
        Date expireDate = JwtTokenUtil.getExpireDate(token);
        // 获取倒计时
        Integer countdown = jwtProperties.getRefreshTokenCountdown();
        // 如果(当前时间+倒计时) > 过期时间，则表示到了倒计时时间（但还未过期），需要刷新token
        boolean refresh = DateUtils.addSeconds(new Date(), countdown).after(expireDate);
        if (!refresh) {
            return;
        }
        // 如果token还没过期，Redis缓存中没有，则表示token已经失效，需要重新登录生成新的token
        boolean exists = loginRedisService.exists(token);
        AssertUtils.isTrue(exists, ShiroStatusMsg.INVALID_TOKEN);

        String username = jwtToken.getUsername();
        String salt = jwtToken.getSalt();
        // 生成新token字符串
        String newToken = JwtTokenUtil.generateToken(username, salt);
        // 生成新JwtToken对象
        Long expireSecond = jwtProperties.getExpireSecond();
        JwtToken newJwtToken = JwtToken.build(newToken, username, salt, expireSecond);
        // 更新redis缓存
        loginRedisService.refreshLoginInfo(token, username, newJwtToken);
        // 设置token刷新后的响应头
        httpServletResponse.setStatus(200);
        httpServletResponse.setHeader(JwtTokenWebUtil.getTokenName(), newToken);
    }
}

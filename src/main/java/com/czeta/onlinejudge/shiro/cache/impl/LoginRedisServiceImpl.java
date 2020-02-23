package com.czeta.onlinejudge.shiro.cache.impl;

import com.czeta.onlinejudge.shiro.cache.LoginRedisService;
import com.czeta.onlinejudge.shiro.consts.RedisKeyConstant;
import com.czeta.onlinejudge.shiro.convert.ShiroMapstructConvert;
import com.czeta.onlinejudge.shiro.enums.ShiroStatusMsg;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.shiro.jwt.JwtToken;
import com.czeta.onlinejudge.shiro.model.result.JwtTokenRedisModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserModel;
import com.czeta.onlinejudge.shiro.model.result.LoginUserRedisModel;
import com.czeta.onlinejudge.util.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * @ClassName LoginRedisServiceImpl
 * @Description 登陆信息Redis缓存服务类
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Slf4j
@Service
public class LoginRedisServiceImpl implements LoginRedisService {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void cacheLoginInfo(JwtToken jwtToken, LoginUserModel loginUserModel) {
        AssertUtils.notNull(jwtToken, ShiroStatusMsg.INVALID_TOKEN);
        AssertUtils.notNull(loginUserModel, ShiroStatusMsg.PARAM_ERROR);
        // token
        String token = jwtToken.getToken();
        // token md5值
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 登陆用户名称
        String username = loginUserModel.getUsername();
        // Redis缓存JWT Token信息
        JwtTokenRedisModel jwtTokenRedisModel = ShiroMapstructConvert.INSTANCE.jwtTokenToJwtTokenRedisModel(jwtToken);
        // Redis缓存登陆用户信息
        LoginUserRedisModel loginUserRedisModel = ShiroMapstructConvert.INSTANCE.loginSysUserVoToLoginUserRedisModel(loginUserModel);
        loginUserRedisModel.setSalt(jwtToken.getSalt());
        // 判断是否启用单个用户登陆，如果是，这每个用户只有一个有效token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            deleteUserAllCache(username);
        }
        // Redis过期时间与JwtToken过期时间一致
        Duration expireDuration = Duration.ofSeconds(jwtToken.getExpireSecond());
        // 1. tokenMd5:jwtTokenRedisVo
        String loginTokenRedisKey = String.format(RedisKeyConstant.LOGIN_TOKEN, tokenMd5);
        redisTemplate.opsForValue().set(loginTokenRedisKey, jwtTokenRedisModel, expireDuration);
        // 2. username:loginSysUserRedisVo
        redisTemplate.opsForValue().set(String.format(RedisKeyConstant.LOGIN_USER, username), loginUserRedisModel, expireDuration);
        // 3. login user token
        redisTemplate.opsForValue().set(String.format(RedisKeyConstant.LOGIN_USER_TOKEN, username, tokenMd5), loginTokenRedisKey, expireDuration);
        log.info("LoginRedisServiceImpl cacheLoginInfo redis_key: LOGIN_USER={} LOGIN_USER_TOKEN={} LOGIN:TOKEN={}",
                String.format(RedisKeyConstant.LOGIN_USER, username),
                String.format(RedisKeyConstant.LOGIN_USER_TOKEN, username, tokenMd5),
                loginTokenRedisKey);
    }

    @Override
    public void refreshLoginInfo(String oldToken, String username, JwtToken newJwtToken) {
        // 删除对应用户的登录有关的Redis缓存
        deleteUserLoginInfo(oldToken, username);
        // 缓存登陆信息
        cacheLoginInfo(newJwtToken, getLoginUserRedisModel(username));
    }

    @Override
    public LoginUserRedisModel getLoginUserRedisModel(String username) {
        AssertUtils.notBlank(username, ShiroStatusMsg.PARAM_ERROR, "username不能为空");
        return (LoginUserRedisModel) redisTemplate.opsForValue().get(String.format(RedisKeyConstant.LOGIN_USER, username));
    }

    @Override
    public void deleteUserLoginInfo(String token, String username) {
        AssertUtils.notBlank(token, ShiroStatusMsg.INVALID_TOKEN, "token不能为空");
        AssertUtils.notBlank(username, ShiroStatusMsg.PARAM_ERROR, "username不能为空");
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(String.format(RedisKeyConstant.LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(RedisKeyConstant.LOGIN_USER, username));
        // 4. delete user token
        redisTemplate.delete(String.format(RedisKeyConstant.LOGIN_USER_TOKEN, username, tokenMd5));
        log.info("LoginRedisServiceImpl deleteUserLoginInfo redis_key: LOGIN_USER={} LOGIN_USER_TOKEN={} LOGIN:TOKEN={}",
                String.format(RedisKeyConstant.LOGIN_USER, username),
                String.format(RedisKeyConstant.LOGIN_USER_TOKEN, username, tokenMd5),
                String.format(RedisKeyConstant.LOGIN_TOKEN, tokenMd5));
    }

    @Override
    public void deleteUserAllCache(String username) {
        Set<String> userTokenMd5Set = redisTemplate.keys(String.format(RedisKeyConstant.LOGIN_USER_TOKEN, username, "*"));
        if (CollectionUtils.isEmpty(userTokenMd5Set)) {
            return;
        }
        // 1. 删除登陆用户的所有token信息
        List<String> tokenMd5List = redisTemplate.opsForValue().multiGet(userTokenMd5Set);
        redisTemplate.delete(tokenMd5List);
        // 2. 删除登陆用户的所有user:token信息
        redisTemplate.delete(userTokenMd5Set);
        // 3. 删除登陆用户信息
        redisTemplate.delete(String.format(RedisKeyConstant.LOGIN_USER, username));
    }

    @Override
    public String getSalt(String username) {
        AssertUtils.notBlank(username, ShiroStatusMsg.PARAM_ERROR, ShiroStatusMsg.PARAM_ERROR + " username不能为空");
        LoginUserRedisModel loginUserRedisModel = (LoginUserRedisModel) redisTemplate.opsForValue().get(String.format(RedisKeyConstant.LOGIN_USER, username));
        return loginUserRedisModel.getSalt();
    }

    @Override
    public boolean exists(String token) {
        AssertUtils.notBlank(token, ShiroStatusMsg.INVALID_TOKEN);
        String tokenMd5 = DigestUtils.md5Hex(token);
        Object object = redisTemplate.opsForValue().get(String.format(RedisKeyConstant.LOGIN_TOKEN, tokenMd5));
        return object != null;
    }
}

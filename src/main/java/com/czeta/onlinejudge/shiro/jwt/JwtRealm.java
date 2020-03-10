package com.czeta.onlinejudge.shiro.jwt;


import com.czeta.onlinejudge.shiro.cache.LoginRedisService;
import com.czeta.onlinejudge.shiro.enums.ShiroStatusMsg;
import com.czeta.onlinejudge.shiro.model.result.LoginUserRedisModel;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * @ClassName JwtRealm
 * @Description Shiro认证与授权
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
public class JwtRealm extends AuthorizingRealm {

    private LoginRedisService loginRedisService;

    public JwtRealm(LoginRedisService loginRedisService) {
        this.loginRedisService = loginRedisService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * 登陆认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        AssertUtils.notNull(jwtToken, ShiroStatusMsg.PARAM_ERROR, "jwtToken不能为空");
        String salt = jwtToken.getSalt();
        AssertUtils.notBlank(salt, ShiroStatusMsg.PARAM_ERROR, "salt不能为空");
        return new SimpleAuthenticationInfo(
                jwtToken,
                salt,
                getName()
        );
    }

    /**
     * 授权认证,设置角色/权限信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        JwtToken jwtToken = (JwtToken) principalCollection.getPrimaryPrincipal();
        String username = jwtToken.getUsername();
        // 从缓存中获取登陆用户角色权限信息
        LoginUserRedisModel loginUserRedisModel = loginRedisService.getLoginUserRedisModel(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 设置角色
        authorizationInfo.setRoles(SetUtils.hashSet(loginUserRedisModel.getRoleName()));
        // 设置权限
        authorizationInfo.setStringPermissions(loginUserRedisModel.getPermissionCodes());
        return authorizationInfo;
    }
}

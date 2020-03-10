package com.czeta.onlinejudge.shiro.config;

import com.czeta.onlinejudge.shiro.cache.LoginRedisService;
import com.czeta.onlinejudge.shiro.consts.CommonConstant;
import com.czeta.onlinejudge.shiro.enums.ShiroStatusMsg;
import com.czeta.onlinejudge.shiro.jwt.JwtCredentialsMatcher;
import com.czeta.onlinejudge.shiro.jwt.JwtFilter;
import com.czeta.onlinejudge.shiro.jwt.JwtProperties;
import com.czeta.onlinejudge.shiro.jwt.JwtRealm;
import com.czeta.onlinejudge.shiro.service.LoginService;
import com.czeta.onlinejudge.utils.exception.APIRuntimeException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.*;

/**
 * @ClassName ShiroConfig
 * @Description Shiro配置
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties({
        ShiroProperties.class
})
public class ShiroConfig {

    /**
     * Jwt Token验证是否有效
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new JwtCredentialsMatcher();
    }

    /**
     * JWT数据源验证
     * @param loginRedisService
     * @return
     */
    @Bean
    public JwtRealm jwtRealm(LoginRedisService loginRedisService) {
        JwtRealm jwtRealm = new JwtRealm(loginRedisService);
        jwtRealm.setCachingEnabled(false);
        jwtRealm.setCredentialsMatcher(credentialsMatcher());
        return jwtRealm;
    }

    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    public DefaultSubjectDAO subjectDAO() {
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return defaultSubjectDAO;
    }

    /**
     * 安全管理器配置
     * @param loginRedisService
     * @return
     */
    @Bean
    public SecurityManager securityManager(LoginRedisService loginRedisService) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm(loginRedisService));
        securityManager.setSubjectDAO(subjectDAO());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * Shiro过滤链配置
     * @param securityManager
     * @param shiroProperties
     * @param jwtProperties
     * @param loginRedisService
     * @param loginService
     * @return
     */
    @Bean(CommonConstant.SHIRO_FILTER_NAME)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroProperties shiroProperties,
                                                         JwtProperties jwtProperties,
                                                         LoginRedisService loginRedisService,
                                                         LoginService loginService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = getFilterMap(jwtProperties, loginRedisService, loginService);
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainMap = getFilterChainDefinitionMap(shiroProperties);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 获取filter map
     * @param jwtProperties
     * @param loginRedisService
     * @param loginService
     * @return
     */
    private Map<String, Filter> getFilterMap(JwtProperties jwtProperties,
                                             LoginRedisService loginRedisService,
                                             LoginService loginService) {
        Map<String, Filter> filterMap = new LinkedHashMap();
        filterMap.put(CommonConstant.JWT_FILTER_NAME, new JwtFilter(jwtProperties, loginRedisService, loginService));
        return filterMap;
    }


    /**
     * Shiro路径权限配置
     * @param shiroProperties
     * @return
     */
    private Map<String, String> getFilterChainDefinitionMap(ShiroProperties shiroProperties) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        List<ShiroPermissionProperties> permissionConfigs = shiroProperties.getPermission();
        if (CollectionUtils.isNotEmpty(permissionConfigs)) {
            for (ShiroPermissionProperties permissionConfig : permissionConfigs) {
                String url = permissionConfig.getUrl();
                String[] urls = permissionConfig.getUrls();
                String permission = permissionConfig.getPermission();
                if (StringUtils.isBlank(url) && ArrayUtils.isEmpty(urls)) {
                    throw new APIRuntimeException(ShiroStatusMsg.PARAM_ERROR, "shiro permission config 路径配置不能为空");
                }
                if (StringUtils.isBlank(permission)) {
                    throw new APIRuntimeException(ShiroStatusMsg.PARAM_ERROR, "shiro permission config permission不能为空");
                }

                if (StringUtils.isNotBlank(url)) {
                    filterChainDefinitionMap.put(url, permission);
                }
                if (ArrayUtils.isNotEmpty(urls)) {
                    for (String string : urls) {
                        filterChainDefinitionMap.put(string, permission);
                    }
                }
            }
        }
        // 如果启用shiro，则设置最后一个设置为JWTFilter，否则全部路径放行
        if (shiroProperties.isEnable()) {
            filterChainDefinitionMap.put("/**", CommonConstant.JWT_FILTER_NAME);
        } else {
            filterChainDefinitionMap.put("/**", "anon");
        }
        return filterChainDefinitionMap;
    }

    /**
     * ShiroFilter配置
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(CommonConstant.SHIRO_FILTER_NAME);
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return filterRegistrationBean;
    }

    @Bean
    public Authenticator authenticator(LoginRedisService loginRedisService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(jwtRealm(loginRedisService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 启用shiro权限管理注解
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}

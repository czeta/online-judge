package com.czeta.onlinejudge.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName CorsConfig
 * @Description 跨域配置
 * @Author chenlongjie
 * @Date 2020/2/23 12:52
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties({
        CorsProperties.class
})
public class CorsConfig {

    /**
     * CORS跨域设置
     *
     * @return
     */
//    @Bean
//    public FilterRegistrationBean corsFilter(CorsProperties corsProperties) {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 跨域配置
//        corsConfiguration.setAllowedOrigins(corsProperties.getAllowedOrigins());
//        corsConfiguration.setAllowedHeaders(corsProperties.getAllowedHeaders());
//        corsConfiguration.setAllowedMethods(corsProperties.getAllowedMethods());
//        corsConfiguration.setAllowCredentials(corsProperties.isAllowCredentials());
//        corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
//        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());
//
//        source.registerCorsConfiguration(corsProperties.getPath(), corsConfiguration);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        bean.setEnabled(corsProperties.isEnable());
//        return bean;
//    }

}

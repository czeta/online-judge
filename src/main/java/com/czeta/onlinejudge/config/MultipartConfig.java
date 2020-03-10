package com.czeta.onlinejudge.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.Map;

/**
 * @ClassName MultipartConfig
 * @Description 文件资源配置
 * @Author chenlongjie
 * @Date 2020/3/10 10:40
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties({
        MultipartProperties.class
})
public class MultipartConfig implements WebMvcConfigurer {

    @Autowired
    private MultipartProperties multipartProperties;

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize(DataSize.of(100, DataUnit.MEGABYTES));
        ///设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(100,DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    /**
     * 资源映射：设置访问资源url对应文件在磁盘中的位置，即通过访问url即可访问磁盘中的文件
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(multipartProperties.getResourceAccessPatterns())
                .addResourceLocations("file:" + multipartProperties.getUploadPath());
    }
}

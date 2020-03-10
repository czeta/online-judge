package com.czeta.onlinejudge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName FileOprProperties
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/10 10:35
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "online-judge.multipart")
public class MultipartProperties {

    /**
     * 上传目录
     */
    private String uploadPath;
    /**
     * 资源访问路径，前端访问
     */
    private String resourceAccessPath;
    /**
     * 资源访问路径，后段配置，资源映射使用（映射成上传目录）
     */
    private String resourceAccessPatterns;

    /**
     * 允许上传的文件后缀集合
     */
    private List<String> allowUploadFileExtensions;
    /**
     * 允许下载的文件后缀集合
     */
    private List<String> allowDownloadFileExtensions;
}

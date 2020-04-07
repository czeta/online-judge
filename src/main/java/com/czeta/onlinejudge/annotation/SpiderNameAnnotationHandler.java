package com.czeta.onlinejudge.annotation;

import com.czeta.onlinejudge.spider.SpiderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SpiderNameAnnotationScan
 * @Description 扫描现有爬虫并注册到可用爬虫map中，供service使用
 * @Author chenlongjie
 * @Date 2020/4/5 14:51
 * @Version 1.0
 */
@Slf4j
@Data
@Component
public class SpiderNameAnnotationHandler {
    // 爬虫名称与爬虫实例的映射，实现横向扩展
    public final Map<String, SpiderService> spiderServiceMap = new HashMap<>();

    {
        Reflections reflections = new Reflections("com.czeta.onlinejudge.spider");
        Set<Class<?>> services =
                reflections.getTypesAnnotatedWith(SpiderName.class);
        for (Class<?> serviceClass : services) {
            SpiderName spiderName = serviceClass.getAnnotation(SpiderName.class);
            try {
                spiderServiceMap.put(spiderName.name(), (SpiderService) serviceClass.newInstance());
            } catch (InstantiationException e) {
                log.error("SpiderNameAnnotationHandler InstantiationException Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            } catch (IllegalAccessException e) {
                log.error("SpiderNameAnnotationHandler IllegalAccessException Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            }
        }
    }
}

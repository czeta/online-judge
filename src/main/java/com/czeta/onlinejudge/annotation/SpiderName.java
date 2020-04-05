package com.czeta.onlinejudge.annotation;

import java.lang.annotation.*;

/**
 * @ClassName SpiderName
 * @Description 爬虫名注解，用于扫描现有爬虫并注册到可用爬虫map中，供service使用
 * @Author chenlongjie
 * @Date 2020/4/5 14:47
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpiderName {
    String name() default "";
}
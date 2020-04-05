package com.czeta.onlinejudge.utils.spider.processor;

import com.czeta.onlinejudge.utils.spider.response.SpiderResponse;

/**
 * @ClassName SpiderPageProcessor
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 12:08
 * @Version 1.0
 */
public interface SpiderPageProcessor {
    void process(SpiderResponse response);
}

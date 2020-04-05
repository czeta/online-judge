package com.czeta.onlinejudge.spider.poj;

import com.czeta.onlinejudge.annotation.SpiderName;
import com.czeta.onlinejudge.spider.SpiderService;

/**
 * @ClassName POJSpiderExecutor
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/5 14:54
 * @Version 1.0
 */

@SpiderName(name = "POJ")
public class POJSpiderExecutor implements SpiderService {
    @Override
    public Object execute(Object obj) {
        String problemId = (String) obj;
        // ...
        return null;
    }

}
package com.czeta.onlinejudge.spider.poj;

import com.czeta.onlinejudge.annotation.SpiderName;
import com.czeta.onlinejudge.spider.SpiderService;
import com.czeta.onlinejudge.utils.spider.SpiderUtils;
import com.czeta.onlinejudge.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudge.utils.spider.request.SpiderRequest;

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
        SpiderRequest request = SpiderRequest.build("http://poj.org/problem?id=" + problemId);
        request.setMethod(SpiderConstant.Method.GET);
        return SpiderUtils.exec(request, new POJProblemSpider());
    }
}
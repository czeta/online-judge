package com.czeta.onlinejudge.spider.poj;

import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.ProblemLanguage;
import com.czeta.onlinejudge.model.result.SpiderProblemResultModel;
import com.czeta.onlinejudge.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudge.utils.spider.response.SpiderResponse;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * @ClassName POJProblemSpider
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/5 14:54
 * @Version 1.0
 */
public class POJProblemSpider implements SpiderPageProcessor {
    @Override
    public Object process(SpiderResponse response) {
        Document doc = response.getDocument();
        AssertUtils.notNull(doc, BaseStatusMsg.APIEnum.FAILED);
        String title = doc.selectFirst("div[class=ptt]").text();
        Elements limits = doc.select("div[class=plm]>table[align=center]>tbody>tr>td");
        String timeLimit = limits.get(0).ownText();
        timeLimit = timeLimit.substring(0, timeLimit.lastIndexOf("MS"));
        String memoryLimit = limits.get(2).ownText();
        memoryLimit = memoryLimit.substring(0, memoryLimit.lastIndexOf("K"));
        memoryLimit = String.valueOf(Integer.valueOf(memoryLimit) / 1024);
        Elements msg = doc.select("div[class=ptx]");
        String description = msg.get(0).html();
        String input = msg.get(1).html();
        String output = msg.get(2).html();
        String hint = msg.get(3).html();
        Elements samples = doc.select("pre[class=sio]");
        String sampleInput = samples.get(0).html();
        String sampleOutput = samples.get(1).html();
        // 返回实体
        SpiderProblemResultModel spiderProblemResultModel = new SpiderProblemResultModel();
        spiderProblemResultModel.setTitle(title);
        spiderProblemResultModel.setTimeLimit(Integer.valueOf(timeLimit));
        spiderProblemResultModel.setMemoryLimit(Integer.valueOf(memoryLimit));
        spiderProblemResultModel.setDescription(description);
        spiderProblemResultModel.setInputDescription(input);
        spiderProblemResultModel.setOutputDescription(output);
        spiderProblemResultModel.setInputSamples(sampleInput);
        spiderProblemResultModel.setOutputSamples(sampleOutput);
        spiderProblemResultModel.setHint(hint);
        // 暂时只添加系统中允许的语言
        spiderProblemResultModel.setLanguage(ProblemLanguage.getAllProblemLanguage());
        return spiderProblemResultModel;
    }
}

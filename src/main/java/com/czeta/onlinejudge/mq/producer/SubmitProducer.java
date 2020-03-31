package com.czeta.onlinejudge.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudge.mq.SubmitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName SubmitProducer
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/29 17:34
 * @Version 1.0
 */
@Component
@Slf4j
public class SubmitProducer {
    private static final String TOPIC_NAME = "topic-submit";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(SubmitMessage submitMessage) {
        kafkaTemplate.send(TOPIC_NAME, JSONObject.toJSONString(submitMessage));
    }
}

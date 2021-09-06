package com.kenji.event;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.kenji.enums.KlineType;
import com.kenji.model.MessagePayload;
import com.kenji.rocket.Source;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:05
 * @Description  K 线推送事件
 */
@Component
@Slf4j
@Data
public class KlineEvent implements Runnable, Event {
    /**
     * 交易对标识符
     */
    private String symbol;
    /**
     * 通道
     */
    private String channel;
    /**
     * redis key 前缀
     */
    private String keyPrefix;


    private static final String KLINE_GROUP = "market.%s.line.%s";  //1 %s: 交易对 2%s: k线类型

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Source source;

    public KlineEvent() {
    }

    public KlineEvent(String symbol, String channel, String keyPrefix) {
        this.symbol = symbol;
        this.channel = channel;
        this.keyPrefix = keyPrefix;
    }

    /**
     * 事件触发处理机制
     */
    @Override
    public void handle() {
        //1 循环所有K线的类型
        for (KlineType klineType : KlineType.values()) {
            // 2 获取特定的K线类型，keyPrefix:etcgcn:1min
            String key = new StringBuffer(keyPrefix).append(symbol.toLowerCase()).append(":").append(klineType.getValue().toLowerCase()).toString();
            List<String> lines = redisTemplate.opsForList().range(key, 0, 1);
            if (!CollectionUtils.isEmpty(lines)){
                String lineData = lines.get(0);
                MessagePayload messagePayload = new MessagePayload();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tick", JSON.parseArray(lineData).toString());
                //market.%s.line.%s
                messagePayload.setChannel(String.format(KLINE_GROUP,symbol.toLowerCase(),klineType.getValue().toLowerCase()));
                messagePayload.setBody(jsonObject.toString());
                //发送k线
                Message<MessagePayload> message = MessageBuilder.withPayload(messagePayload).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
                source.subscribeEventOutput().send(message);
            }
        }
    }

    /**
     * 让线程池调度
     */
    @Override
    public void run() {
        handle();
    }
}

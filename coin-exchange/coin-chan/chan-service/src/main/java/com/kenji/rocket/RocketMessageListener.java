package com.kenji.rocket;

import com.alibaba.fastjson.JSON;
import com.kenji.model.MessagePayload;
import com.kenji.model.ResponseEntity;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

/**
 * @Author Kenji
 * @Date 2021/8/27 22:30
 * @Description
 */
@Component
@Slf4j
public class RocketMessageListener {

    @Autowired
    private TioWebSocketServerBootstrap bootstrap;

    @StreamListener("tio_group")
    public void handlerMessage(MessagePayload message){
        log.info("rocketmq 接收到消息: {}", JSON.toJSONString(message));
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCh(message.getChannel());
        responseEntity.put("result",message.getBody());
        //推送给前端用户
        if (StringUtils.hasText(message.getUserId())){
            Tio.sendToUser(bootstrap.getServerTioConfig(),message.getUserId(),responseEntity.build());
            return;
        }
        @NonNull String group = message.getChannel();
        Tio.sendToGroup(bootstrap.getServerTioConfig(),group, responseEntity.build());
    }
}

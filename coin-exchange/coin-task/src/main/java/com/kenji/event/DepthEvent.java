package com.kenji.event;

import com.alibaba.fastjson.JSONObject;
import com.kenji.dto.MarketDto;
import com.kenji.enums.DepthMergeType;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.model.MessagePayload;
import com.kenji.rocket.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:04
 * @Description 深度盘口数据事件
 */
@Component
@Slf4j
public class DepthEvent implements Event {

    @Autowired
    private Source source;

    private static final String DEPTH_GROUP = "market.%s.depth.step%s"; //1:%s 具体的那个交易对 %s深度的类型

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    /**
     * 推送市场合并深度
     */
    @Override
    public void handle() {
        //1 查询市场
        List<MarketDto> marketDtoList =  marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtoList)){
            return;
        }
        for (MarketDto marketDto : marketDtoList) {
            String symbol = marketDto.getSymbol();  //交易对名称
            //查询该交易对所有的深度数据/盘口数据
            for (DepthMergeType mergeType : DepthMergeType.values()) {
                //2 通过交易对以及合并的类型查询所有的深度数据
                String data = marketServiceFeign.depthData(symbol,mergeType.getValue());
                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(String.format(DEPTH_GROUP,symbol.toLowerCase(),mergeType.getValue()));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tick",data);
                messagePayload.setBody(jsonObject.toJSONString());
                Message<MessagePayload> message = MessageBuilder.withPayload(messagePayload).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
                source.subscribeEventOutput().send(message);
            }
        }


    }
}

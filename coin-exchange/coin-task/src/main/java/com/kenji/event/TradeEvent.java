package com.kenji.event;

import com.alibaba.fastjson.JSONObject;
import com.kenji.dto.MarketDto;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.model.MessagePayload;
import com.kenji.rocket.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:07
 * @Description  成交记录事件
 */
@Component
@Slf4j
public class TradeEvent implements Event {

    @Autowired
    private Source source;

    private static final String TRADE_GROUP = "market.%s.trade.detail"; // %s 代表交易对

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    @Override
    public void handle() {
        //获取所有交易市场
        List<MarketDto> marketDtoList = marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtoList)){
            return;
        }
        for (MarketDto marketDto : marketDtoList) {
            //查询该交易对下的交易数据
            String data = marketServiceFeign.trades(marketDto.getSymbol());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data",data);
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setChannel(String.format(TRADE_GROUP,marketDto.getSymbol().toLowerCase()));
            messagePayload.setBody(jsonObject.toString());
            Message<MessagePayload> message = MessageBuilder.withPayload(messagePayload).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
            source.subscribeEventOutput().send(message);
        }

    }
}

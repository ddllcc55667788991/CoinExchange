package com.kenji.event;

import com.alibaba.fastjson.JSONObject;
import com.kenji.dto.MarketDto;
import com.kenji.dto.TradeAreaDto;
import com.kenji.dto.TradeMarketDto;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.feign.TradingAreaServiceClient;
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
 * @Date 2021/8/28 0:08
 * @Description  行情市场的 K 线
 */
@Component
@Slf4j
public class MarketEvent implements Event {

    @Autowired
    private Source source;

    @Autowired
    private TradingAreaServiceClient tradingAreaServiceClient;

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    private static final String MARKET_GROUP = "market.%s.ticker"; // %s 代表交易区域

    private static final String MARKET_DETAIL_GROUP = "markets.%s.detail" ; //交易区域的详情交易数据



    @Override
    public void handle() {
        //1 交易区域的查询
        List<TradeAreaDto> tradeAreaDtoList = tradingAreaServiceClient.tradeAreaList();
        if (CollectionUtils.isEmpty(tradeAreaDtoList)){
            return;
        }
        for (TradeAreaDto tradeAreaDto : tradeAreaDtoList) {
            //该交易区域下的交易数据，使用的是交易区域里面的市场id("id1","id2")
            List<TradeMarketDto> tradeMarketDtos = marketServiceFeign.queryMarketsByIds(tradeAreaDto.getMarketIds());
            if (CollectionUtils.isEmpty(tradeMarketDtos)){
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("markets",tradeMarketDtos);
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setBody(jsonObject.toString());
            messagePayload.setChannel(String.format(MARKET_GROUP,tradeAreaDto.getCode().toLowerCase()));
            Message<MessagePayload> message = MessageBuilder.withPayload(messagePayload).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
            source.subscribeEventOutput().send(message);
        }

        //获取所有交易市场
        List<MarketDto> marketDtoList = marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtoList)){
            return;
        }
        for (MarketDto marketDto : marketDtoList) {
            List<TradeMarketDto> tradeMarketDtos = marketServiceFeign.queryMarketsByIds(marketDto.getId().toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tick",tradeMarketDtos);
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setBody(jsonObject.toJSONString());
            messagePayload.setChannel(String.format(MARKET_DETAIL_GROUP,marketDto.getSymbol().toLowerCase()));
            Message<MessagePayload> message = MessageBuilder.withPayload(messagePayload).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
            source.subscribeEventOutput().send(message);
        }

    }
}

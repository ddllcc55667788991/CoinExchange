package com.kenji.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.kenji.model.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.http.StringSplitUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Objects;

/**
 * @Author Kenji
 * @Date 2021/8/27 20:04
 * @Description
 */
@Component
@Slf4j
public class WebSocketMessageHandler implements IWsMsgHandler {

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request 参数等
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientIp = httpRequest.getClientIp();
        log.info("收到来自{}的ws握手包\r\n{}",clientIp,httpRequest.toString());
        return httpResponse;
    }

    /**
     * 握手成功后走的方法
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        log.info("和客户端握手成功");
    }

    /**
     * 字节消息(binaryType = arraybuffer) 过来后会走这个方法
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext,"remove channelContext");
        return null;
    }

    /**
     * 字符消息(binaryType = blob) 过来后会走这个方法
     * @param wsRequest
     * @param text
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {

        if (Objects.equals("ping",text)){
            return "pong";
        }
        log.info(text);
        //订阅消息ch 为 为订阅的频道 id 为订阅唯一标识
        JSONObject payload = JSONObject.parseObject(text);
        String sub = payload.getString("sub");  //要订阅的组
        String req = payload.getString("req");  //当前的request(保留字段)
        String cancel = payload.getString("cancel");    //要取消订阅的组
        String id = payload.getString("id");    //订阅的id(保留字段)
        //如果用户已登录（有authorization数据），同时绑定用户
        String authorization = payload.getString("authorization");

        if (StringUtils.hasText(sub)) {
            Tio.bindGroup(channelContext, sub);  //绑定群组
        }

        if (StringUtils.hasText(cancel)){
            Tio.unbindGroup(cancel,channelContext);
        }


        if (StringUtils.hasText(authorization) &&authorization.startsWith("bearer ")){
            String token = authorization.replaceAll("bearer ", "");
            Jwt jwt = JwtHelper.decode(token);
            String jwtJsonStr = jwt.getClaims();
            JSONObject jwtJson = JSON.parseObject(jwtJsonStr);
            String userId = jwtJson.getString("user_name");
            Tio.bindUser(channelContext,userId);    //绑定用户
        }
            return new ResponseEntity()
                    .setId(id)
                    .setSubbed(sub)
                    .setStatus("ok")
                    .setCanceled(cancel)
                    .setCh(sub)
                    .setEvent(req)
                    .build();
    }
}

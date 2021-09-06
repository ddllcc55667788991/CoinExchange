package com.kenji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.EnableTioWebSocketServer;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.Date;

/**
 * @Author Kenji
 * @Date 2021/8/27 20:02
 * @Description
 */
@SpringBootApplication
@EnableTioWebSocketServer   //开启tio的websocket
@EnableScheduling
public class ChanApplication {


    public static void main(String[] args) {
        SpringApplication.run(ChanApplication.class,args);
    }


}

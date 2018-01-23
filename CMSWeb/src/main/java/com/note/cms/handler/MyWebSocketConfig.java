package com.note.cms.handler;

/**
 * Created by hasee on 2017/11/27.
 */
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
//@EnableWebMvc
@EnableWebSocket
@Controller
public class MyWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //前台 可以使用websocket环境
        registry.addHandler(myWebSocketHandler(),"/websocket.do");

        //前台 不可以使用websocket环境，则使用sockjs进行模拟连接
        registry.addHandler(myWebSocketHandler(), "/sockjs/websocket.do").withSockJS();

        // 用来注册websocket server实现类，第二个参数是访问websocket的地址
//        registry.addHandler(systemWebSocketHandler(), "/webSocketServer.do").addInterceptors(new HandshakeInterceptor());
        registry.addHandler(systemWebSocketHandler(), "/webSocketServer.do");
        // 使用Sockjs的注册方法
        registry.addHandler(systemWebSocketHandler(), "/sockjs/webSocketServer.do").withSockJS();
    }


    // websocket 处理类
    @Bean
    public WebSocketHandler myWebSocketHandler(){
        return new MyWebSocketHandler();
    }

    @Bean
    public WebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }



}
package org.project.springbootstarter.configuration;

import org.project.springbootstarter.service.QuestionServiceImpl;
import org.project.springbootstarter.service.RedisServiceImpl;
import org.project.springbootstarter.websocket.HandshakeInterceptor;
import org.project.springbootstarter.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private RedisServiceImpl redisService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/events/{sessionId}")
                .addInterceptors(new HandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler(questionService, redisService);
    }
}
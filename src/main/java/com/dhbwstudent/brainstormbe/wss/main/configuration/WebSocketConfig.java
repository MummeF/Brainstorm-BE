package com.dhbwstudent.brainstormbe.wss.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com")
//        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .setHandshakeHandler(new HandshakeHandler());
//                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com")
//        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .setHandshakeHandler(new HandshakeHandler())
                .withSockJS();
//                .addInterceptors(new HttpSessionHandshakeInterceptor())
    }
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
}
package com.dhbwstudent.brainstormbe.wss.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/topic/notification").permitAll()
                .simpDestMatchers("/**").authenticated()
                .anyMessage().denyAll();

    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com")
        registry.addEndpoint("/ws").setAllowedOrigins("*")
        .setHandshakeHandler(new HandshakeHandler());
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com")
        registry.addEndpoint("/ws").setAllowedOrigins("*")
        .setHandshakeHandler(new HandshakeHandler()).withSockJS();
    }
}
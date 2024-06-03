package com.cmux.postservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
            .setApplicationDestinationPrefixes("/app")
            .enableStompBrokerRelay("/topic", "/queue")
            .setRelayHost("rabbitmq")
            .setRelayPort(61613)
            .setClientLogin("guest")
            .setClientPasscode("guest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-communitypost").setAllowedOriginPatterns("*").withSockJS();
    }
}

package com.cmux.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${RABBITMQ_HOST:localhost}")
    private String rabbitmqHost;

    // use RabbitMQ as message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // registry.enableSimpleBroker("/topic"); // default Stomp message broker
        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(rabbitmqHost)
                // Default port 61613 for RabbitMQ with STOMP plugin
                .setRelayPort(61613) 
                .setClientLogin("guest")
                .setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }
}

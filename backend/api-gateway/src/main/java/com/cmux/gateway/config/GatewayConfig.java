package com.cmux.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import java.util.function.Predicate;

@Configuration
public class GatewayConfig {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route("user-service", r -> r.path("/user/**")
                                                .uri("http://user-service:5002"))
                                .route("auth-service", r -> r.path("/auth/**")
                                                .uri("http://user-service:5002"))
                                .route("ws-chat-handshake", r -> r.path("/ws-chat/info**")
                                                .uri("http://chat-service:8080"))
                                .route("ws-chat-service", r -> r.path("/ws-chat/**")
                                                .uri("ws://chat-service:8080"))
                                .route("chat-service", r -> r.path("/api/chats/**")
                                                .uri("http://chat-service:8080"))
                                .route("ws-post-service", r -> r.path("/ws-communitypost/**")
                                                .uri("http://post-service:9000"))
                                .route("post-comments", r -> r.path("/comments/**")
                                                .uri("http://post-service:9000"))
                                .route("post-community", r -> r.path("/community/**")
                                                .uri("http://post-service:9000"))
                                .route("reward-service", r -> r.path("/shop/**")
                                                .uri("http://reward-service:8081"))
                                .route("subscription-followers", r -> r.path("/followers/**")
                                                .uri("http://subscription-service:8080"))
                                .route("subscription-subscriptions", r -> r.path("/subscriptions/**")
                                                .uri("http://subscription-service:8080"))
                                .build();
        }

}

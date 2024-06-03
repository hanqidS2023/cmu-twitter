package com.cmux.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.reward.routing.key}")
    private String rewardRoutingKey;

    public MQProducer(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

    // When a user recieved a new follower, send a message to the reward service
    // to add 10 points to the user's account
    public void sendNewFollowerMessageToReward(String message) {
        rabbitTemplate.convertAndSend(exchange, rewardRoutingKey, message);
        System.out.println("Sent message to reward service");
    }
}

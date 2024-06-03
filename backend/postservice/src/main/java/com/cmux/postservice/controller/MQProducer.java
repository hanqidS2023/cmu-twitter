package com.cmux.postservice.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.newlike.routing.key}")
    private String rewardRoutingKey;

    public MQProducer (RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendIdToReward(String message) {
        System.out.println("MQProducer: sendIdToReward: sending message to reward service" + message);
        rabbitTemplate.convertAndSend(exchange, rewardRoutingKey, message);
    }
}


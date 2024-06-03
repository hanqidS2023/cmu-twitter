package com.cmux.user.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.fanout.name}")
    private String fanoutExchange;

    public MQProducer (RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNewUserMessage(String message) {
        rabbitTemplate.convertAndSend(fanoutExchange, "", message);
    }
}



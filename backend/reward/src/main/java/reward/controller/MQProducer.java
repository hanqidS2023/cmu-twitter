package reward.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.newicon.routing.key}")
    private String userRoutingKey;

    public MQProducer (RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendImageToUser(String message) {
        rabbitTemplate.convertAndSend(exchange, userRoutingKey, message);
    }
}



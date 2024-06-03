package com.cmux.user.controller;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.cmux.user.dto.PurchaseProductMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.cmux.user.service.UserService;

@Component
public class MQConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserService userService;

    @Autowired
    public MQConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = { "${rabbitmq.queue.name.user.newicon}" })
    public void receiveMessage(String message) throws JsonMappingException, JsonProcessingException {
        PurchaseProductMessage m = mapper.readValue(message, PurchaseProductMessage.class);
        Long userId = m.getUserId();
        Long productId = m.getProductId();
        String imageUrl = m.getImageUrl();
        try {
            userService.addNewIconToUser(userId, imageUrl, productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

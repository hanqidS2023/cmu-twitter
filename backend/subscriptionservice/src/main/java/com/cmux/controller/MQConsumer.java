package com.cmux.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cmux.dto.NewUserDTO;
import com.cmux.repository.UserRepository;
import com.cmux.service.SubscriptionService;

@Component
public class MQConsumer {
    private final SubscriptionService subscriptionService;


    private MQConsumer(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public List<String> extracUsernameAndId (String message) {
        boolean firstColon = false;
        boolean secondColon = false;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (firstColon) {
                sb.append(c);
            }
            if (secondColon) {
                sb2.append(c);
            }
            if (c == ',' && firstColon) {
                firstColon = false;
            }
            if (c == ':') {
                if (!firstColon && sb.length() == 0) {
                    firstColon = true;
                } else {
                    secondColon = true;
                }
            }
        }
        Long id = Long.parseLong(sb.deleteCharAt(sb.length() - 1).toString());
        sb2.delete(0, 1);
        sb2.reverse().delete(0, 3);
        sb2.reverse();
        String username = sb2.toString().substring(1, sb2.length() - 1);
        List<String> result = new ArrayList<>();
        result.add(username);
        // Long to String
        String idString = Long.toString(id);
        result.add(idString);
        return result;
    }

    @RabbitListener(queues = { "${rabbitmq.queue.name.newuser}" })
    public void receiveMessage(String message) throws JsonProcessingException {

        List<String> res = this.extracUsernameAndId(message);
        String username = res.get(0);
        Long id = Long.parseLong(res.get(1));

        try {
            subscriptionService.createUser(id, username);
            System.out.println("User created " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

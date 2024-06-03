package com.cmux.chat.controller;

import com.cmux.chat.model.ChatMessage;
import com.cmux.chat.service.ChatService;
import com.cmux.chat.dto.MessageRequest;
import com.cmux.chat.model.MessageType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.cmux.chat.service.S3Service;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import java.util.UUID;
import java.time.Instant;

@Controller
public class ChatWebSocketController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest chatMessage) {
        ChatMessage newMessage = ChatMessage.builder()
            .chatId(chatMessage.getChatId())
            .senderId(chatMessage.getSenderId())
            .messageType(MessageType.TEXT)
            .messageId(Uuids.timeBased())
            .timestamp(Instant.now())
            .content(chatMessage.getContent())
            .build();
        chatService.saveMessage(newMessage);
        messagingTemplate.convertAndSend("/topic/chat." + chatMessage.getChatId(), newMessage);
    }
}

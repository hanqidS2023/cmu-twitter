package com.cmux.chat.controller;

import com.cmux.chat.dto.PrivateChatRequest;
import com.cmux.chat.dto.GroupChatRequest;
import com.cmux.chat.dto.MessageRequest;
import com.cmux.chat.model.ChatMessage;
import com.cmux.chat.model.Chat;
import com.cmux.chat.model.GroupUser;
import com.cmux.chat.model.UserChat;
import com.cmux.chat.model.ChatType;
import com.cmux.chat.model.MessageType;
import com.cmux.chat.service.ChatService;
import com.cmux.chat.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;
import java.time.Instant;
import com.datastax.oss.driver.api.core.uuid.Uuids;

@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private S3Service s3Service;

    // @PostMapping("/message")
    // public ResponseEntity<ChatMessage> sendMessage(@RequestBody MessageRequest chatMessage) {
    //     ChatMessage newMessage = ChatMessage.builder()
    //         .chatId(chatMessage.getChatId())
    //         .senderId(chatMessage.getSenderId())
    //         .messageType(MessageType.TEXT)
    //         .messageId(Uuids.timeBased())
    //         .timestamp(Instant.now())
    //         .content(chatMessage.getContent())
    //         .build();
    //     return ResponseEntity.ok(chatService.saveMessage(newMessage));
    // }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public ResponseEntity<ChatMessage> sendFile(
            @RequestParam("chatId") UUID chatId,
            @RequestParam("senderId") Long senderId,
            @RequestParam("messageType") MessageType messageType,
            @RequestParam(value = "file") MultipartFile file) {

        String fileUrl = null;
        try {
            fileUrl = s3Service.uploadFile(file);
        } catch (Exception e) {
            System.out.println("Error uploading file to S3");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        ChatMessage newFileMessage = ChatMessage.builder()
                .chatId(chatId)
                .senderId(senderId)
                .messageType(messageType)
                .messageId(Uuids.timeBased())
                .timestamp(Instant.now())
                .fileUrl(fileUrl)
                .build();
        return ResponseEntity.ok(chatService.saveMessage(newFileMessage));
    }

    @PostMapping("/private")
    public ResponseEntity<Chat> getOrCreatePrivateChat(@RequestBody PrivateChatRequest privateChatRequest) {
        return ResponseEntity.ok(chatService.getOrCreatePrivateChat(privateChatRequest.getUser1Id(), privateChatRequest.getUser2Id()));
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupChat(@RequestBody GroupChatRequest groupChatRequest) {
        Chat newGroupChat = Chat.builder()
                .chatId(UUID.randomUUID())
                .chatType(ChatType.GROUP)
                .chatName(groupChatRequest.getChatName())
                .lastMessageTime(Instant.now())
                .build();
        return ResponseEntity.ok(chatService.createGroupChat(newGroupChat, groupChatRequest.getUserId()));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID chatId) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats() {
        List<Chat> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChatById(@PathVariable UUID chatId) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/history/{chatId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable UUID chatId) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatService.getChatHistory(chatId));
    }

    @GetMapping("/chatlist/{userId}")
    public ResponseEntity<List<Chat>> getChatsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatsByUserId(userId));
    }

    @GetMapping("/groupusers/{chatId}")
    public ResponseEntity<List<Long>> getGroupUsers(@PathVariable UUID chatId) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatService.getGroupUsers(chatId));
    }

    @PostMapping("/groupusers/{chatId}")
    public ResponseEntity<?> addUsersToGroup(@PathVariable UUID chatId, @RequestBody List<Long> userIds) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        if (chat.getChatType() != ChatType.GROUP) {
            return ResponseEntity.badRequest().body("Chat must be a group chat");
        }
        if (userIds == null) {
            return ResponseEntity.badRequest().body("User IDs list must not be null");
        }
        for (Long userId : userIds) {
            if (userId == null) {
                return ResponseEntity.badRequest().body("User IDs in the list must not be null");
            }
            GroupUser groupUser = new GroupUser(chatId, userId);
            chatService.addUserToGroup(groupUser);
            UserChat userChat = new UserChat(userId, chatId);
            chatService.addChatToUser(userChat);
        } 
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/groupusers/{chatId}/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable UUID chatId, @PathVariable Long userId) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        chatService.removeUserFromGroup(chatId, userId);
        return ResponseEntity.ok().build();
    }
}

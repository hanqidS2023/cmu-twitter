package com.cmux.chat.service;

import com.cmux.chat.model.Chat;
import com.cmux.chat.model.ChatMessage;
import com.cmux.chat.model.GroupUser;
import com.cmux.chat.model.UserChat;
import com.cmux.chat.model.PrivateChat;
import com.cmux.chat.model.ChatType;
import com.cmux.chat.model.MessageType;
import com.cmux.chat.repository.ChatMessageRepository;
import com.cmux.chat.repository.ChatRepository;
import com.cmux.chat.repository.PrivateChatRepository;
import com.cmux.chat.repository.GroupUserRepository;
import com.cmux.chat.repository.UserChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Collections;


@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private UserChatRepository userChatRepository;

    public Chat getOrCreatePrivateChat(Long user1Id, Long user2Id) {
        if (user1Id > user2Id) {
            Long temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }
        UUID chatId;
        Optional<PrivateChat> existingPrivateChat = privateChatRepository.findByUser1IdAndUser2Id(user1Id, user2Id);
        if (existingPrivateChat.isPresent()) {
            chatId = existingPrivateChat.get().getChatId();
            return chatRepository.findById(chatId).orElse(null);
        } else {
            chatId = UUID.randomUUID();
            PrivateChat newPrivateChat = PrivateChat.builder()
                               .user1Id(user1Id)
                               .user2Id(user2Id)
                               .chatId(chatId)
                               .build();
            privateChatRepository.save(newPrivateChat);
            Chat newChat = Chat.builder()
                           .chatId(chatId)
                           .chatType(ChatType.PRIVATE)
                           .chatName(user1Id + "-" + user2Id)
                           .build();
            chatRepository.save(newChat);
            UserChat user1Chat = new UserChat(user1Id, chatId);
            UserChat user2Chat = new UserChat(user2Id, chatId);
            userChatRepository.save(user1Chat);
            userChatRepository.save(user2Chat);
            return newChat;
        }
    }

    public Chat createGroupChat(Chat chat, Long userId) {
        Chat savedChat = chatRepository.save(chat);
        UserChat userChat = new UserChat(userId, chat.getChatId());
        userChatRepository.save(userChat);
        GroupUser groupUser = new GroupUser(chat.getChatId(), userId);
        groupUserRepository.save(groupUser);
        return savedChat;
    }

    public void deleteChat(UUID chatId) {
        chatMessageRepository.deleteAllByChatId(chatId);
        chatRepository.deleteById(chatId);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(UUID chatId) {
        return chatRepository.findById(chatId)
                .orElse(null);
    }

    public List<ChatMessage> getChatHistory(UUID chatId) {
        return chatMessageRepository.getChatHistory(chatId);
    }

    public List<Long> getGroupUsers(UUID chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null || chat.getChatType() != ChatType.GROUP) {
            // should throw exception
            return null;
        }
        List<GroupUser> groupUsers = groupUserRepository.findByChatId(chatId);
        List<Long> userIds = groupUsers.stream()
                .map(GroupUser::getUserId)
                .collect(Collectors.toList());
        return userIds;
    }

public List<Chat> getChatsByUserId(Long userId) {
    List<UserChat> userChats = userChatRepository.findByUserId(userId);
    List<UUID> chatIds = userChats.stream()
            .map(UserChat::getChatId)
            .collect(Collectors.toList());
    List<Chat> chats = chatRepository.findAllById(chatIds);
    chats.sort(Comparator.comparing(Chat::getLastMessageTime, Comparator.nullsLast(Comparator.naturalOrder())));
    Collections.reverse(chats);
    return chats;
}

    public void addUserToGroup(GroupUser groupUser) {
        groupUserRepository.save(groupUser);
    }

    public void addChatToUser(UserChat userChat) {
        userChatRepository.save(userChat);
    }

    public void removeUserFromGroup(UUID chatId, Long userId) {
        groupUserRepository.deleteByChatIdAndUserId(chatId, userId);
        userChatRepository.deleteByChatIdAndUserId(chatId, userId);
        long count = groupUserRepository.countByChatId(chatId);
        if (count == 0) {
            chatRepository.deleteById(chatId);
        }
    }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        Chat chat = chatRepository.findById(chatMessage.getChatId()).orElse(null);
        if (chat == null) {
            // should throw exception
            return null;
        }
        chat.setLastMessageTime(chatMessage.getTimestamp());
        MessageType messageType = chatMessage.getMessageType();
        if (messageType == MessageType.TEXT) {
            chat.setLastMessage(chatMessage.getContent());
        } else if (messageType == MessageType.IMAGE) {
            chat.setLastMessage("[Image]");
        } else if (messageType == MessageType.FILE) {
            chat.setLastMessage("[File]");
        }
        chatRepository.save(chat);
        return chatMessageRepository.save(chatMessage);
    }
}

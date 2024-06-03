package com.cmux.chat.repository;

import com.cmux.chat.model.ChatMessage;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends CassandraRepository<ChatMessage, UUID> { 
    @Query("SELECT * FROM chat_messages WHERE chatId = ?0 ORDER BY messageId ASC")
    List<ChatMessage> getChatHistory(UUID chatId);
    
    @Query("DELETE FROM chat_messages WHERE chatid = ?0")
    void deleteAllByChatId(UUID chatId);
}

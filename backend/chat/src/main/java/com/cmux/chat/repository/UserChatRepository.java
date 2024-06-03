package com.cmux.chat.repository;

import com.cmux.chat.model.UserChat;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserChatRepository extends CassandraRepository<UserChat, Long> {
    @Query("DELETE FROM user_chats WHERE chat_id = ?0 AND user_id = ?1")
    void deleteByChatIdAndUserId(UUID chatId, Long userId);

    List<UserChat> findByUserId(Long userId);
}

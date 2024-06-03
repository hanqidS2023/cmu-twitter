package com.cmux.chat.repository;

import com.cmux.chat.model.GroupUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupUserRepository extends CassandraRepository<GroupUser, UUID> {
    @Query("DELETE FROM group_users WHERE chat_id = ?0 AND user_id = ?1")
    void deleteByChatIdAndUserId(UUID chatId, Long userId);

    List<GroupUser> findByChatId(UUID chatId);

    long countByChatId(UUID chatId);
}

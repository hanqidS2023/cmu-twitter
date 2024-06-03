package com.cmux.chat.repository;

import com.cmux.chat.model.PrivateChat;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivateChatRepository extends CassandraRepository<PrivateChat, Long> {
    Optional<PrivateChat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}

package com.cmux.chat.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import java.util.UUID;

@Getter
@Setter
@Builder
@Table("private_chats")
public class PrivateChat {
    @PrimaryKeyColumn(name = "user1_id", type = PARTITIONED)
    private Long user1Id;
    @PrimaryKeyColumn(name = "user2_id", type = CLUSTERED)
    private Long user2Id;
    private UUID chatId;
}

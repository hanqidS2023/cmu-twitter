package com.cmux.chat.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.Ordering.ASCENDING;
import java.util.UUID;
import java.time.Instant;

@Getter
@Setter
@Table("chat_messages")
@Builder
public class ChatMessage {

    @PrimaryKeyColumn(name = "chatid", type = PARTITIONED)
    private UUID chatId;

    @PrimaryKeyColumn(name = "messageid", type = CLUSTERED, ordering = ASCENDING)
    private UUID messageId;

    private Long senderId;
    private Instant timestamp;
    private MessageType messageType;
    private String content;
    private String fileUrl;
}

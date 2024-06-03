package com.cmux.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import static org.springframework.data.cassandra.core.cql.Ordering.ASCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Table("user_chats")
public class UserChat {

    @PrimaryKeyColumn(name = "user_id", type = PARTITIONED)
    private Long userId;

    @PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = CLUSTERED, ordering = ASCENDING)
    private UUID chatId;
}

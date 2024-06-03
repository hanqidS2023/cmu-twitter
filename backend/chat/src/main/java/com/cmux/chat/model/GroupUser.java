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
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Table("group_users")
public class GroupUser {

    @PrimaryKeyColumn(name = "chat_id", type = PARTITIONED)
    private UUID chatId;

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, ordering = ASCENDING)
    private Long userId;
}

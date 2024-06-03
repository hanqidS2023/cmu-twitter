package com.cmux.chat.dto;

import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class PrivateChatRequest {
    @NotNull
    private Long user1Id;
    @NotNull
    private Long user2Id;
}

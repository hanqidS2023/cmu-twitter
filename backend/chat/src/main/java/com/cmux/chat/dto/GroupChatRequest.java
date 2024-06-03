package com.cmux.chat.dto;

import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class GroupChatRequest {
    @NotNull
    private String chatName;
    @NotNull
    private Long userId;
}

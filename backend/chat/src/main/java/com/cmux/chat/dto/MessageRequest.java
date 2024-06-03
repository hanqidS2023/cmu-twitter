package com.cmux.chat.dto;

import com.cmux.chat.model.MessageType;
import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class MessageRequest {
    @NotNull
    private UUID chatId;
    @NotNull
    private Long senderId;
    private String content;
}

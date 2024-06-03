package com.cmux.user.dto;

import lombok.Data;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserMessage {
    private Long userId;
    private String username;
}

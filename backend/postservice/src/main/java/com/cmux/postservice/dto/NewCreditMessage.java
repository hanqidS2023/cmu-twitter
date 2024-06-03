package com.cmux.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCreditMessage {
    private Long userId;
    private int amount;
}

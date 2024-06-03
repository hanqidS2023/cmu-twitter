package com.cmux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class NewUserDTO {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;
}


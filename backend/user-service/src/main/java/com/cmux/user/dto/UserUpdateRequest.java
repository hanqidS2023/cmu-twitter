package com.cmux.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String email;
    private String userImage;
    private List<String> unlockedImages;
    private List<Long> unlockedImageIds;
    private String bio;
}

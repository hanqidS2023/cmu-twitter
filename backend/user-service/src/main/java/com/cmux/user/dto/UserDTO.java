package com.cmux.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String userImage;
    private List<String> unlockedImages;
    private List<Long> unlockedImageIds;
    private LocalDateTime createdAt;
    private String bio;

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public List<String> getUnlockedImages() {
        return unlockedImages;
    }

    public void setUnlockedImages(List<String> unlockedImages) {
        this.unlockedImages = unlockedImages;
    }

    public List<Long> getUnlockedImageIds() {
        return unlockedImageIds;
    }

    public void setUnlockedImageIds(List<Long> unlockedImageIds) {
        this.unlockedImageIds = unlockedImageIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }  
}



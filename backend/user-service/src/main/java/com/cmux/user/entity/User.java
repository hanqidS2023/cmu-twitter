package com.cmux.user.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_username", columnList = "username"),
    @Index(name = "idx_user_email", columnList = "email")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userImage;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> unlockedImages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> unlockedImageIds = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String bio;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userImage = "https://cmux-reward.s3.us-east-2.amazonaws.com/base.png";
        this.unlockedImages.add(this.userImage);
        this.unlockedImages.add("https://cmux-reward.s3.us-east-2.amazonaws.com/intermediate.png");
        this.bio = "I am a CMU-X user!";
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


    public void setPassword(String password) {
        this.password = password;
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

    @JsonIgnore
    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}

package com.cmux.user.dto;

public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private long userId;
    private String username;

    public JwtResponse(String accessToken, String refreshToken, long userId, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

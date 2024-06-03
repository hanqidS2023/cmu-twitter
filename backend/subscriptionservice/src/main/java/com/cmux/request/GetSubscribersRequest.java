package com.cmux.request;

public class GetSubscribersRequest {

    private Long userId; // User ID for whom to get the subscribers

    public GetSubscribersRequest() {
        
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

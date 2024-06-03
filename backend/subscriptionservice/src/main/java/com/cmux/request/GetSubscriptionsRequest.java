package com.cmux.request;

public class GetSubscriptionsRequest {

    private Long userId; // User ID for whom to get the subscriptions

    public GetSubscriptionsRequest() {
        // Default constructor
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

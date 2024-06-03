package com.cmux.request;

public class SubscribeRequest {

    // Define the properties of the subscription, such as user ID and subscription details
    private Long userId;
    private Long userIdSubscribeTo;

    public SubscribeRequest(Long userId, Long userIdSubscribeTo) {
        this.userId = userId;
        this.userIdSubscribeTo = userIdSubscribeTo;
    }

    // Getters and setters for each field
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserIdSubscribeTo() {
        return userIdSubscribeTo;
    }

    public void setUserIdSubscribeTo(Long userIdSubscribeTo) {
        this.userIdSubscribeTo = userIdSubscribeTo;
    }
}

package com.cmux.request;

public class UnsubscribeRequest {

    // Define the properties of the subscription, such as user ID and subscription details
    private Long userId;
    private Long userIdAnother;

    public UnsubscribeRequest(Long userId, Long userIdAnother) {
        this.userId = userId;
        this.userIdAnother = userIdAnother;
    }

    // Getters and setters for each field
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserIdAnother() {
        return userIdAnother;
    }

    public void setUserIdSubscribeTo(Long userIdAnother) {
        this.userIdAnother = userIdAnother;
    }
}

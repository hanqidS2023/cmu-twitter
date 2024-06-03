package com.cmux.service.strategy.subscription;

import java.util.List;
import com.cmux.entity.User;

public interface SubscriptionStrategy {
    void addSubscription(Long userId, Long subscriptionId);
    void removeSubscription(Long userId, Long subscriptionId);
    List<User> getAllFollowers(Long userId);
    List<User> getAllSubscriptions(Long userId);
    List<User> getAllMutualSubscriptions(Long userId);
    boolean getHasSubscription(Long userId, Long otherUserId);
}

package com.cmux.service.strategy.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.cmux.entity.User;
import com.cmux.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class Subscription implements SubscriptionStrategy {

    @Autowired
    UserRepository userRepository;

    @Override
    public void addSubscription(Long userId, Long subscriptionId) {
        try {
            // Assuming userRepository.addSubscription checks if both users exist
            userRepository.addSubscription(userId, subscriptionId);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException");
        } catch (ConstraintViolationException e) {
            System.out.println("ConstraintViolationException");
        }

    }

    @Override
    public void removeSubscription(Long userId, Long subscriptionId) {
		userRepository.removeSubscription(userId, subscriptionId);
    }

    @Override
    public List<User> getAllFollowers(Long userId) {
		return userRepository.findFollowersByUserId(userId);
    }

    @Override
    public List<User> getAllSubscriptions(Long userId) {
		return userRepository.findSubscriptionsByUserId(userId);
    }

    @Override
    public List<User> getAllMutualSubscriptions(Long userId) {
		return userRepository.findMutualSubscriptionsByUserId(userId);
    }

    @Override
    public boolean getHasSubscription(Long userId, Long otherUserId) {
		return userRepository.findHasSubscriptionByUserId(userId, otherUserId) != null;
    }

}

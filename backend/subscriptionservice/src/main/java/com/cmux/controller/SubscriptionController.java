package com.cmux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cmux.entity.User;
import com.cmux.request.CreateUserRequest;
import com.cmux.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
    
    @Autowired
    private MQProducer messageProducer;

    @Autowired
    private  SubscriptionService subscriptionService;

    // Get all subscribers for a specific user
    @GetMapping("/followers")
    public List<User> getAllFollowers(@RequestParam("userId") Long userId) {
        logger.info("Get all followers for user {}", userId);
        return subscriptionService.getAllFollowers(userId);
    }

    @GetMapping("/followers/mutual")
    public List<User> getAllMutualFollowers(@RequestParam("userId") Long userId) {
        logger.info("Get all mutual followers for user {}", userId);
        return subscriptionService.getAllMutualSubscriptions(userId);
    }

    // Get numbers of subscribers for a specific user
    @GetMapping("/followers/count")
    public int getFollowersCount(@RequestParam("userId") Long userId) {
        logger.info("Get number of followers for user {}", userId);
        return subscriptionService.getAllFollowers(userId).size();
    }

    // Get all subscriptions for a specific user
    @GetMapping("/subscriptions")
    public List<User> getAllSubscriptions(@RequestParam("userId") Long userId) {
        logger.info("Get all subscriptions for user {}", userId);
        return subscriptionService.getAllSubscriptions(userId);
    }

    // Add a subscription for a user
    @PutMapping("/subscriptions")
    public void addSubscription(@RequestParam("userId") Long userId,
            @RequestParam("otherUserId") Long otherUserId) throws JsonProcessingException {
        System.out.println("Add subscription for user " + userId);
        System.out.println("Add subscription to user " + otherUserId);
        subscriptionService.addSubscription(userId, otherUserId);
    }

    // Create a new user with name and userId
    @PostMapping("/subscriptions")
    public void createUser(@RequestParam("userId") Long userId, @RequestParam("username") String username) {
        System.out.println("Create user with userId " + userId);
        subscriptionService.createUser(userId, username);
    }

    // Remove a subscription from a user
    @DeleteMapping("/subscriptions")
    public void removeSubscription(@RequestParam("userId") Long userId, @RequestParam("otherUserId") Long otherUserIdq) {
        subscriptionService.removeSubscription(userId, otherUserIdq);
    }

    // Get specific users
    @GetMapping("/subscriptions/users")
    public List<User> getUser(@RequestParam("u") String u) {
        System.out.println("Get user " + u);
        return subscriptionService.getUsers(u);
    }

    // Get numbers of subscribers for a specific user
    @GetMapping("/subscriptions/count")
    public int getSubscriptionsCount(@RequestParam("userId") Long userId) {
        System.out.println("Get number of subscriptions for user " + userId);
        return subscriptionService.getAllSubscriptions(userId).size();
    }

    // Get whether the user with userId is subscribed to the user with otherUserId
    @GetMapping("/subscriptions/has")
    public boolean getHasSubscription(@RequestParam("userId") Long userId,
            @RequestParam("otherUserId") Long otherUserId) {
        System.out.println("Get whether user " + userId + " has subscription to user " + otherUserId);
        return subscriptionService.getHasSubscription(userId, otherUserId);
    }

}

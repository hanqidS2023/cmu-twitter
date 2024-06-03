package com.cmux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmux.controller.MQProducer;
import com.cmux.dto.NewCreditMessage;
import com.cmux.entity.User;
import com.cmux.repository.UserRepository;
import com.cmux.service.strategy.subscription.SubscriptionStrategy;
import com.cmux.service.strategy.usercreation.UserCreationStrategy;
import com.cmux.service.strategy.userretrieval.UserRetrievalStrategy;
import com.cmux.service.strategy.userretrieval.UserRetrievalbyID;
import com.cmux.service.strategy.userretrieval.UserRetrievalbyName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class SubscriptionService {


    private final UserRepository userRepository;
    private UserCreationStrategy userCreationStrategy;
    private SubscriptionStrategy subscriptionStrategy;

    private final ObjectMapper mapper = new ObjectMapper();
    private final MQProducer messageProducer;

    private final int REWARD_AMOUNT = 10;


    public SubscriptionService(SubscriptionStrategy subscriptionStrategy, 
    UserCreationStrategy userCreationStrategy, 
    MQProducer messageProducer,
    UserRepository userRepository) {
        this.subscriptionStrategy = subscriptionStrategy;
		this.userCreationStrategy = userCreationStrategy;
        this.messageProducer = messageProducer;
        this.userRepository = userRepository;
    }
	
    public void addSubscription(Long userId, Long subscriptionId) throws JsonProcessingException {
        try {
            subscriptionStrategy.addSubscription(userId, subscriptionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewCreditMessage creditMessage = new NewCreditMessage(subscriptionId, REWARD_AMOUNT);
        String jsonString = mapper.writeValueAsString(creditMessage);
        System.out.println("Sending reward: "+jsonString);
        messageProducer.sendNewFollowerMessageToReward(jsonString);
        
    }

    public void removeSubscription(Long userId, Long subscriptionId) {
        subscriptionStrategy.removeSubscription(userId, subscriptionId);
    }

    public List<User> getAllFollowers(Long userId) {
        return subscriptionStrategy.getAllFollowers(userId);
    }

    public List<User> getAllSubscriptions(Long userId) {
        return subscriptionStrategy.getAllSubscriptions(userId);
    }

    public List<User> getAllMutualSubscriptions(Long userId) {
        return subscriptionStrategy.getAllMutualSubscriptions(userId);
    }

    public boolean getHasSubscription(Long userId, Long otherUserId) {
        return subscriptionStrategy.getHasSubscription(userId, otherUserId);
    }

	public List<User> getUsers(String u) {
        // if name is all numbers, then search by userId
        UserRetrievalStrategy userRetrievalStrategy = new UserRetrievalbyName(userRepository);
        if (u.matches("[0-9]+")) {
            userRetrievalStrategy = new UserRetrievalbyID(userRepository);
        }
        return userRetrievalStrategy.getUsers(u);
	}

	public void createUser(Long userId, String name) {
		userCreationStrategy.createUser(userId, name);
	}


}

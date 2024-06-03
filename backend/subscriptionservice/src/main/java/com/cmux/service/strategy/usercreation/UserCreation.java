package com.cmux.service.strategy.usercreation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmux.repository.UserRepository;
import com.cmux.entity.User;

@Service
public class UserCreation implements UserCreationStrategy {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(Long userId, String name) {
        return userRepository.createUser(userId, name);
    }
}

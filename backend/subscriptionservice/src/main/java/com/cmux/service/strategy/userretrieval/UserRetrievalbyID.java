package com.cmux.service.strategy.userretrieval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cmux.repository.UserRepository;
import com.cmux.entity.User;
import java.util.List;

@Component
public class UserRetrievalbyID implements UserRetrievalStrategy {

    private UserRepository userRepository;

    public UserRetrievalbyID(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers(String userIdString) {
        try {
            Long userId = Long.parseLong(userIdString);
            return userRepository.getUserByUserId(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}

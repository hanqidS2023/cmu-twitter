package com.cmux.service.strategy.userretrieval;

import java.util.List;
import com.cmux.entity.User;

public interface UserRetrievalStrategy {
    List<User> getUsers(String u);

}

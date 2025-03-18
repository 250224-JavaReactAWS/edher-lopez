package com.revature.repos;

import com.revature.models.User;

public interface UserDAO extends GeneralDAO<User>{
    public User getUserByEmail(String userName);
}

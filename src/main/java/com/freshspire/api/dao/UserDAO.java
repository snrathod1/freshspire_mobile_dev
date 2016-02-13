package com.freshspire.api.dao;

import com.freshspire.api.model.User;

public interface UserDAO {

    void addUser(User user);
    void updateUser(User user);
    void deleteUser(String userId);
}

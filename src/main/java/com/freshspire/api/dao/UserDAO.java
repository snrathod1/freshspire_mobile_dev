package com.freshspire.api.dao;

import com.freshspire.api.model.User;

public interface UserDAO {

    void addUser(User user);
    void updateUser(User user);
    User getUser(String userId);
    void deleteUser(String userId);
    User getUserByPhoneNumber(String phoneNumber);
    User getUserByApiKey(String apiKey);
}

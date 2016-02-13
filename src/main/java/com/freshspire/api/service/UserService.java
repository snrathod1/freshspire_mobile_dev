package com.freshspire.api.service;

import com.freshspire.api.model.User;

public interface UserService {

    void addUser(User p);
    void updateUser(User p);
    User getUserById(String userId);
    void deleteUser(String userId, String authKey);
    boolean authenticateUser(String userId, String authKey);
}

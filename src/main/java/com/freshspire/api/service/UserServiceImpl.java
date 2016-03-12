package com.freshspire.api.service;

import com.freshspire.api.dao.UserDAO;
import com.freshspire.api.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public void addUser(User user) {
        this.userDAO.addUser(user);
    }

    @Transactional
    public void updateUser(User user) {
        this.userDAO.updateUser(user);
    }

    @Transactional
    public User getUserById(String userId) {
        return this.userDAO.getUserById(userId);
    }

    @Transactional
    public void deleteUser(String userId, String authKey) {
        this.userDAO.deleteUser(userId);
    }

    /**
     * @param userId
     * @param apiKey
     * @return
     */
    public boolean authenticateUser(String userId, String apiKey) {
        User userByApiKey = this.userDAO.getUserByApiKey(apiKey);
        User userByUserId = this.userDAO.getUserById(userId);

        return userByApiKey == userByUserId;
    }

    @Transactional
    public User getUserByPhoneNumber(String phoneNumber) {
        return this.userDAO.getUserByPhoneNumber(phoneNumber);
    }

    public boolean userExistsWithPhoneNumber(String phoneNumber) {
        return (this.userDAO.getUserByPhoneNumber(phoneNumber) != null);
    }

    @Transactional
    public User getUserByApiKey(String apiKey) {
        return this.userDAO.getUserByApiKey(apiKey);
    }

    @Transactional
    public boolean userExistsWithApiKey(String apiKey) {
        return (this.userDAO.getUserByApiKey(apiKey) != null);
    }
}

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
        return this.userDAO.getUser(userId);
    }

    @Transactional
    public void deleteUser(String userId, String authKey) {
        this.userDAO.deleteUser(userId);
    }

    /**
     * TODO: Implement this
     * @param userId
     * @param authKey
     * @return
     */
    public boolean authenticateUser(String userId, String authKey) {
        return false;
    }

    @Transactional
    public boolean doesUserExistForNumber(String phoneNumber) {
        User user = this.userDAO.getUserByPhoneNumber(phoneNumber);
        return user == null ? false : true;
    }

    @Transactional
    public User getUserByPhoneNumber(String phoneNumber) {
        return this.userDAO.getUserByPhoneNumber(phoneNumber);
    }

    @Transactional
    public User getUserByApiKey(String apiKey) {
        return this.userDAO.getUserByApiKey(apiKey);
    }
}

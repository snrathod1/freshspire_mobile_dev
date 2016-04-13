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
    @Override
    public void addUser(User user) {
        this.userDAO.addUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        this.userDAO.updateUser(user);
    }

    @Transactional
    @Override
    public User getUserById(int userId) {
        return this.userDAO.getUserById(userId);
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        this.userDAO.deleteUser(userId);
    }

    @Override
    public boolean authenticateUser(int userId, String apiKey) {
        User user = this.userDAO.getUserById(userId);
        if(user == null) {
            return false;
        } else {
            return user.getApiKey().equals(apiKey);
        }
    }

    @Transactional
    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return this.userDAO.getUserByPhoneNumber(phoneNumber);
    }

    @Transactional
    @Override
    public User getUserByApiKey(String apiKey) {
        return this.userDAO.getUserByApiKey(apiKey);
    }

}

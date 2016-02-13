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

    public User getUserById(String userId) {
        return null;
    }

    public void deleteUser(String userId, String authKey) {
        if (authenticateUser(userId, authKey)) {
            this.userDAO.deleteUser(userId);
        }
    }

    public boolean authenticateUser(String userId, String authKey) {
        return false;
    }
}

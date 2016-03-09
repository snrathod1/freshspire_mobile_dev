package com.freshspire.api.dao;

import com.freshspire.api.model.User;
import org.hibernate.SessionFactory;

import java.util.List;

public interface UserDAO {

    void addUser(User user);
    void updateUser(User user);
    User getUserById(String userId);
    void deleteUser(String userId);
    User getUserByPhoneNumber(String phoneNumber);
    User getUserByApiKey(String apiKey);
    List<User> getAdmins();
    void setSessionFactory(SessionFactory sessionFactory);
}

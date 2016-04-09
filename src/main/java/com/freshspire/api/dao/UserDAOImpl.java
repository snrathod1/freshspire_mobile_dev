package com.freshspire.api.dao;

import com.freshspire.api.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = getCurrentSession();
        session.persist(user);
        logger.info("User saved : " + user);
    }

    @Override
    public void updateUser(User user) {
        Session session = getCurrentSession();
        session.update(user);
        logger.info("User updated : " + user);
    }

    @Override
    public void deleteUser(int userId) {
        Session session = getCurrentSession();
        User user = (User) session.load(User.class, userId);
        if(null != user){
            session.delete(user);
        }
        logger.info("User deleted : " + user);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User U where U.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", phoneNumber);
        User result = (User) query.uniqueResult();

        return result;
    }

    @Override
    public User getUserByApiKey(String apiKey) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User U where U.apiKey = :apiKey");
        query.setParameter("apiKey", apiKey);
        User result = (User) query.uniqueResult();
        return result;
    }

    @Override
    public User getUserById(int userId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User U where U.userId = :userId");
        query.setParameter("userId", userId);
        User result = (User) query.uniqueResult();

        return result;
    }

    @Override
    public List<User> getAdmins() {
        Session session = getCurrentSession();
        Query query = session.createQuery("from User U where U.admin = :admin");
        query.setParameter("admin", new Boolean(true));
        return query.list();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

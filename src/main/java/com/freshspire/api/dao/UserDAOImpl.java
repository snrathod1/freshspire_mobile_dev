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

    public void addUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
        logger.info("User saved : " + user);
    }

    public void updateUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(user);
        logger.info("User updated : " + user);
    }

    public void deleteUser(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, userId);
        if(null != user){
            session.delete(user);
        }
        logger.info("User deleted : " + user);
    }

    public User getUser(String userId) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User U where U.userId = :userId");
        query.setParameter("userId", userId);
        User result = (User) query.uniqueResult();

        return result;
    }
}

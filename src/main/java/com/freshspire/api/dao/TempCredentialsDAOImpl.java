package com.freshspire.api.dao;


import com.freshspire.api.model.TempCredentials;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TempCredentialsDAOImpl implements TempCredentialsDAO {

    private static final Logger logger = LoggerFactory.getLogger(TempCredentialsDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTempCredentials(TempCredentials creds) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(creds);
        logger.info("TempCredentials saved: " + creds);
    }

    public void updateTempCredentials(TempCredentials creds) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(creds);
        logger.info("TempCredentials updated: " + creds);
    }

    public TempCredentials getTempCredentials(String phoneNumber) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery("from TempCredentials creds where creds.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", phoneNumber);
        return (TempCredentials) query.uniqueResult();
    }
}

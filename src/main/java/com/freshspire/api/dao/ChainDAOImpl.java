package com.freshspire.api.dao;

import com.freshspire.api.model.Chain;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ChainDAOImpl implements ChainDAO {

    private static final Logger logger = LoggerFactory.getLogger(ChainDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Chain getChainById(int chainId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Chain C where C.chainId = :chainId");
        query.setParameter("chainId", chainId);
        return (Chain) query.uniqueResult();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

package com.freshspire.api.dao;

import com.freshspire.api.model.Discount;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiscountDAOImpl implements DiscountDAO {

    private static final Logger logger = LoggerFactory.getLogger(StoreDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    public Discount getDiscountById(String discountId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Discount D where D.discountId = :discountId");
        query.setParameter("discountId", discountId);
        return (Discount) query.uniqueResult();
    }

    public List<Discount> getDiscountByLatLong(
            float latitude,
            float longitude,
            String queryParam,
            float within,
            String foodType,
            String chain) {
        Session session = getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append("From Discount D, Store S, Product P where D.storeId = S.storeId and D.productId = P.productId");
        Query query = session.createQuery(queryString.toString());

        return query.list();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

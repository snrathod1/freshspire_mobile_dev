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


    @Override
    public Discount getDiscountById(int discountId) {
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
        queryString.append("SELECT *, ( 3959 * acos( cos( radians( " + latitude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) + sin( radians( " + latitude + ") ) * sin( radians( latitude ) ) ) ) AS distance FROM");
        queryString.append(" discount");
        queryString.append(" INNER JOIN stores ON stores.storeId = discount.storeId INNER JOIN product on product.productId = discount.productId WHERE");
        queryString.append(" product.name like '%" + queryParam + "%'");
        queryString.append(" and product.foodType like '%" + foodType + "%'");
        queryString.append(" HAVING distance < " + within  + " ORDER BY distance;");

        Query query = session.createSQLQuery(queryString.toString());

        return query.list();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

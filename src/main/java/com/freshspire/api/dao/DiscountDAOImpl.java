package com.freshspire.api.dao;

import com.freshspire.api.model.Chain;
import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Product;
import com.freshspire.api.model.Store;
import com.google.common.base.Strings;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionSystemException;

import java.sql.SQLException;
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

    public List getDiscountByLatLong(
            float latitude,
            float longitude,
            String queryParam,
            float within,
            List<String> foodTypes,
            String chain) {
        Session session = getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT *, ( 3959 * acos( cos( radians( "
                + latitude
                + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("
                + longitude + ") ) + sin( radians( "
                + latitude + ") ) * sin( radians( latitude ) ) ) ) AS distance FROM");
        queryString.append(" discount");
        queryString.append(" INNER JOIN stores ON stores.storeId = discount.storeId");
        queryString.append(" INNER JOIN product on product.productId = discount.productId");
        queryString.append(" INNER JOIN chains on chains.chainId = discount.chainId");

        boolean isEmptyParam = Strings.isNullOrEmpty(queryParam);
        boolean isEmptyFoodTypes = foodTypes == null || foodTypes.size() == 0;
        boolean isEmptyChain = Strings.isNullOrEmpty(chain);

        if (!isEmptyParam || !isEmptyFoodTypes || !isEmptyChain) {
            queryString.append(" WHERE");

            // flag to check if 'and' has to be prefixed before clause
            boolean andFlag = false;

            if (!isEmptyParam) {
                queryString.append(" product.displayName like '%" + queryParam + "%'");
                andFlag = true;
            }

            if (!isEmptyFoodTypes) {
                if (andFlag) {
                    queryString.append(" and");
                }
                for(int i = 0; i < foodTypes.size(); i++) {
                    // don't append the OR to the last foodType
                    if(i == foodTypes.size() - 1) {
                        queryString.append(" product.foodType like '%" + foodTypes.get(i) + "%'");
                    } else {
                        queryString.append(" product.foodType like '%" + foodTypes.get(i) + "%' or");
                    }
                }
                andFlag = true;
            }

            if (!isEmptyChain) {
                if (andFlag) {
                    queryString.append(" and");
                }
                queryString.append(" chains.displayName like '%" + chain + "%'");
            }
        }

        queryString.append(" HAVING distance < " + within  + " ORDER BY distance;");

        Query query = session.createSQLQuery(queryString.toString())
                .addEntity(Discount.class)
                .addEntity(Store.class)
                .addEntity(Product.class)
                .addEntity(Chain.class)
                .addScalar("distance");
        return query.list();
    }

    @Override
    public void addDiscount(Discount discount) {
        Session session = getCurrentSession();

        session.persist(discount);

        logger.info("Added discount: " + discount);
    }

    @Override
    public boolean connectionIsEstablished() {
        return getCurrentSession().isConnected();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

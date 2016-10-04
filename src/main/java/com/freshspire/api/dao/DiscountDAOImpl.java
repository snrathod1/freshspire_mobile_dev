package com.freshspire.api.dao;

import com.freshspire.api.model.Chain;
import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Product;
import com.freshspire.api.model.Store;
import com.google.common.base.Strings;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
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

    public List getDiscountByLatLong(
            float latitude,
            float longitude,
            String queryParam,
            float within,
            List<String> foodTypes,
            List<String> chains) {
        Session session = getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT discount.discountId,"
                + "discount.storeId,"
                + "discount.productId,"
                + "discount.posted,"
                + "discount.expirationDate,"
                + "discount.originalPrice,"
                + "discount.discountedPrice,"
                + "discount.quantity,"
                + "discount.unit,"
                + "stores.displayName AS storeName,"
                + "stores.street,"
                + "stores.city,"
                + "stores.state,"
                + "stores.zipCode,"
                + "stores.latitude,"
                + "stores.longitude,"
                + "product.displayName AS productName,"
                + "product.foodType,"
                + "product.thumbnail AS productThumb,"
                + "chains.displayName AS chainName,"
                + "chains.chainId,"
                + "( 3959 * acos( cos( radians( "
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
        boolean isEmptyChain = chains == null || chains.size() == 0;

        if (!isEmptyParam || !isEmptyFoodTypes || !isEmptyChain) {
            StringBuilder whereClause = new StringBuilder(" WHERE");

            // flag to check if 'and' has to be prefixed before clause
            boolean andFlag = false;

            if (!isEmptyParam) {
                whereClause.append(" (product.displayName like '%" + queryParam + "%')");
                andFlag = true;
            }

            if (!isEmptyFoodTypes) {
                if (andFlag) {
                    whereClause.append(" and (");
                }
                for(int i = 0; i < foodTypes.size(); i++) {
                    // don't append the OR to the last foodType
                    if(i == foodTypes.size() - 1) {
                        whereClause.append(" product.foodType like '%" + foodTypes.get(i) + "%')");
                    } else {
                        whereClause.append(" product.foodType like '%" + foodTypes.get(i) + "%' or");
                    }
                }
                andFlag = true;
            }

            if (!isEmptyChain) {
                if (andFlag) {
                    whereClause.append(" and (");
                }
                for(int i = 0; i < chains.size(); i++) {
                    // don't append OR to the last chain
                    if(i == chains.size() - 1) {
                        whereClause.append(" chains.chainId = " + chains.get(i) + ")");
                    } else {
                        whereClause.append(" chains.chainId = " + chains.get(i) + " or");
                    }
                }
            }
            queryString.append(whereClause.toString());
        }

        queryString.append(" HAVING distance < " + within  + " ORDER BY distance;");
        System.out.println("query: " + queryString.toString());
        // This is a hacky way of doing things. Rewrite the queryString, maybe as a HQL string, in a way that's alias-
        // friendly. The reason for all these addScalar()s is because multiple tables have a displayName column, so we
        // must manually map the result set to the desired objects.
        Query query = session.createSQLQuery(queryString.toString())
                .addScalar("discountId")
                .addScalar("storeId")
                .addScalar("productId")
                .addScalar("posted")
                .addScalar("expirationDate")
                .addScalar("originalPrice")
                .addScalar("discountedPrice")
                .addScalar("quantity")
                .addScalar("unit")
                .addScalar("storeName")
                .addScalar("street")
                .addScalar("city")
                .addScalar("state")
                .addScalar("zipCode")
                .addScalar("latitude", new DoubleType())
                .addScalar("longitude", new DoubleType())
                .addScalar("productName")
                .addScalar("foodType")
                .addScalar("productThumb")
                .addScalar("chainName")
                .addScalar("chainId")
                .addScalar("distance");
        query.setResultTransformer(new DiscountSearchResultTransformer());
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

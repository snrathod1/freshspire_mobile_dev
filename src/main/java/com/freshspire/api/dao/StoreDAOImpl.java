package com.freshspire.api.dao;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.google.common.base.Strings;

import java.util.List;

@Repository
public class StoreDAOImpl implements StoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(StoreDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    //TODO : Add implementation to add a new store to database
    @Override
    public void addStore(Store store) {

    }

    //TODO : Add implementation to update an existing store
    @Override
    public void updateStore(Store store) {

    }

    /**
     * Fetch store by store id, this is a unique store result
     * @param storeId the id of the store to be fetched
     * @return
     */
    @Override
    public Store getStoreById(int storeId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Store S where S.storeId = :storeId");
        query.setParameter("storeId", storeId);
        Store store = (Store) query.uniqueResult();

        return store;
    }

    //TODO: get lat long from zipcode and then call getStoreByLocation
    @Override
    public List<Store> getStoreByZip(int zipcode) {
        return null;
    }

    public List<Store> getStoreByLocation(double latitude, double longitude) {
        Session session = getCurrentSession();
        Query query = session.createSQLQuery("SELECT *, ( 3959 * acos( cos( radians(37) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(-122) ) + sin( radians(37) ) * sin( radians( latitude ) ) ) ) AS distance FROM stores HAVING distance < 25 ORDER BY distance LIMIT 0 , 20;");
        Object obj = query.list();
        return query.list();
    }

    @Override
    public List<Discount> getDiscountsByStoreId(int storeId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Discount D where D.storeId = :storeId");
        query.setParameter("storeId", storeId);

        return query.list();
    }

    public List<Store> getStores() {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Store");
        return query.list();
    }

    public List<Discount> getDiscountsByStoreId(int storeId, String queryParam, String foodType) {
        Session session = getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append("From Discount D, Product P where D.storeId = :storeId ");

        boolean isQueryParamEmpty = Strings.isNullOrEmpty(queryParam);
        boolean isFoodTypeEmpty = Strings.isNullOrEmpty(foodType);

        if ( !(isQueryParamEmpty && isFoodTypeEmpty)) {
            queryString.append("and D.productId = P.productId ");
        }
        if (!isQueryParamEmpty) {
            queryString.append("and P.name = :query ");
        }
        if (!isFoodTypeEmpty) {
            queryString.append("and P.foodType = :foodType ");
        }
        Query query = session.createQuery(queryString.toString());

        query.setParameter("storeId", storeId);
        if (!isQueryParamEmpty) {
            query.setParameter("query", "%" + queryParam + "%");
        }

        if (!isFoodTypeEmpty) {
            query.setParameter("foodType", foodType);
        }

        return query.list();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

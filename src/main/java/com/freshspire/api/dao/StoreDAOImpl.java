package com.freshspire.api.dao;

import com.freshspire.api.model.Store;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StoreDAOImpl implements StoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(StoreDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    //TODO : Add implementation to add a new store to database
    public void addStore(Store store) {

    }

    //TODO : Add implementation to update an existing store
    public void updateStore(Store store) {

    }

    /**
     * Fetch store by store id, this is a unique store result
     * @param storeId the id of the store to be fetched
     * @return
     */
    public Store getStoreById(String storeId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Store S where S.storeId = :storeId");
        query.setParameter("storeId", storeId);
        Store store = (Store) query.uniqueResult();

        return store;
    }

    //TODO: get lat long from zipcode and then call getStoreByLocation
    public List<Store> getStoreByZip(int zipcode) {
        return null;
    }

    public List<Store> getStoreByLocation(double latitude, double longitude) {
        Session session = getCurrentSession();
        Query query = session.createSQLQuery("SELECT *, ( 3959 * acos( cos( radians(37) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(-122) ) + sin( radians(37) ) * sin( radians( latitude ) ) ) ) AS distance FROM Store HAVING distance < 25 ORDER BY distance LIMIT 0 , 20;");
        return query.list();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}

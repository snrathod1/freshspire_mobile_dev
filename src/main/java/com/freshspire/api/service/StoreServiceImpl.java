package com.freshspire.api.service;

import com.freshspire.api.dao.StoreDAO;
import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private StoreDAO storeDAO;

    public void setStoreDAO(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    @Transactional
    public List<Store> getStores() {
        return storeDAO.getStores();
    }

    @Transactional
    public Store getStore(String storeId) {
        return storeDAO.getStoreById(storeId);
    }

    @Transactional
    public List<Store> getStore(int zipcode) {
        return storeDAO.getStoreByZip(zipcode);
    }

    @Transactional
    public List<Store> getStore(float latitude, float longitude) {
        return storeDAO.getStoreByLocation(latitude, longitude);
    }

    @Transactional
    public List<Discount> getDiscounts(String storeId) {
        return storeDAO.getDiscountsByStoreId(storeId);
    }

    @Transactional
    public List<Discount> getDiscounts(String storeId, String query, String foodType) {
        return storeDAO.getDiscountsByStoreId(storeId, query, foodType);
    }
}

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
    @Override
    public List<Store> getStores() {
        return storeDAO.getStores();
    }

    @Transactional
    @Override
    public Store getStoreById(int storeId) {
        return storeDAO.getStoreById(storeId);
    }

    @Transactional
    @Override
    public List<Store> getStoresByZipCode(int zipcode) {
        return storeDAO.getStoreByZip(zipcode);
    }

    @Transactional
    @Override
    public List<Store> getStoresByLatLong(float latitude, float longitude) {
        return storeDAO.getStoreByLocation(latitude, longitude);
    }

    @Transactional
    @Override
    public List<Discount> getDiscounts(int storeId) {
        return storeDAO.getDiscountsByStoreId(storeId);
    }

    @Transactional
    public List<Discount> getDiscounts(int storeId, String query, String foodType) {
        return storeDAO.getDiscountsByStoreId(storeId, query, foodType);
    }
}

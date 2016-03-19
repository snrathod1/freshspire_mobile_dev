package com.freshspire.api.service;

import com.freshspire.api.dao.StoreDAO;
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
        return null;
    }

    @Transactional
    public Store getStore(String storeId) {
        return null;
    }

    @Transactional
    public List<Store> getStore(int zipcode) {
        return null;
    }

    @Transactional
    public List<Store> getStore(float latitude, float longitude) {
        return null;
    }
}

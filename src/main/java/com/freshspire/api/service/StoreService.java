package com.freshspire.api.service;

import com.freshspire.api.model.Store;

import java.util.List;

public interface StoreService {

    List<Store> getStores();
    Store getStore(String storeId);
    List<Store> getStore(int zipcode);
    List<Store> getStore(float latitude, float longitude);
}

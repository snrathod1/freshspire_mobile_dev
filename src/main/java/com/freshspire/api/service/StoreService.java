package com.freshspire.api.service;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;

import java.util.List;

public interface StoreService {

    List<Store> getStores();
    Store getStoreById(int storeId);
    List<Store> getStoresByZipCode(int zipcode);
    List<Store> getStoresByLatLong(float latitude, float longitude);
    List<Discount> getDiscounts(int storeId);
    List<Discount> getDiscounts(int storeId, String query, String foodType);

    void addStore(Store store);
}

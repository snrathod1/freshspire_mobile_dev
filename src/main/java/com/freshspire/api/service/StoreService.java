package com.freshspire.api.service;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;

import java.util.List;

public interface StoreService {

    List<Store> getStores();
    Store getStoreById(int storeId);
    List<Store> getStoresByZipCode(int zipcode);
    List<Store> getStoresByLatLong(float latitude, float longitude);
    List<Discount> getDiscounts(String storeId);
    List<Discount> getDiscounts(String storeId, String query, String foodType);
}

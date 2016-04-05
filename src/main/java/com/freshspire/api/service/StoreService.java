package com.freshspire.api.service;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;

import java.util.List;

public interface StoreService {

    List<Store> getStores();
    Store getStoreById(String storeId);
    List<Store> getStoresByZipCode(int zipcode);
    List<Store> getStoresByLatLong(float latitude, float longitude);
    List<Discount> getDiscountsInStore(String storeId);
}

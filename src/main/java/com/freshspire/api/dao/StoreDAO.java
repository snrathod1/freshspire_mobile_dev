package com.freshspire.api.dao;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;

import java.util.List;

public interface StoreDAO {

    void addStore(Store store);
    void updateStore(Store store);
    Store getStoreById(String storeId);
    List<Store> getStoreByZip(int zipcode);
    List<Store> getStoreByLocation(double latitude, double longitude);
    List<Discount> getDiscountsByStoreId(String storeId);
}

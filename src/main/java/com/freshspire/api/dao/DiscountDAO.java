package com.freshspire.api.dao;

import com.freshspire.api.model.Discount;

import java.util.List;

public interface DiscountDAO {

    Discount getDiscountById(int discountId);
    List<Object> getDiscountByLatLong(float latitude, float longitude, String queryParam, float within,
                                      List<String> foodTypes, String chain);

    void addDiscount(Discount discount);

    boolean connectionIsEstablished();
}

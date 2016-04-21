package com.freshspire.api.service;

import com.freshspire.api.model.Discount;

import java.util.List;

public interface DiscountService {

    Discount getDiscountById(int discountId);
    List getDiscountsByLatLong(float latitude, float longitude, String queryParam, float within, String foodType, String chain);

    void addDiscount(Discount discount);
}

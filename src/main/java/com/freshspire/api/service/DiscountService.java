package com.freshspire.api.service;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.response.DiscountData;

import java.util.List;

public interface DiscountService {

    Discount getDiscountById(int discountId);
    List<DiscountData> getDiscountsByLatLong(float latitude, float longitude, String queryParam, float within,
                                             List<String> foodTypes, String chain);

    void addDiscount(Discount discount);

    boolean isUp();
}

package com.freshspire.api.dao;

import com.freshspire.api.model.Discount;

public interface DiscountDAO {

    Discount getDiscountById(String discountId);
}

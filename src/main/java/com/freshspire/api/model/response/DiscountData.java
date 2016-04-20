package com.freshspire.api.model.response;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Product;
import com.freshspire.api.model.Store;

public class DiscountData {
    private Discount discount;
    private Store store;
    private Product product;
    private double distance;

    public DiscountData(Object[] discountData) {
        for(Object object : discountData) {

            if (object instanceof Discount) {
                this.discount = (Discount) object;
            } else if (object instanceof Store) {
                this.store = (Store) object;
            } else if (object instanceof Product) {
                this.product = (Product) object;
            } else if (object instanceof Double) {
                this.distance = (Double) object;
            }
        }
    }
}

package com.freshspire.api.model.param;

/**
 * Parameters for a new discount to be added to the DB layer.
 */
public class NewDiscountParams {

    private String storeId;

    private String productId;

    private double originalPrice;

    private double discountedPrice;

    private String expires; // TODO what data type? How does the REST controller handle dates?

    public NewDiscountParams(String storeId, String productId, double originalPrice, double discountedPrice, String expires) {
        this.storeId = storeId;
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.expires = expires;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}

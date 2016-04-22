package com.freshspire.api.model.param;

/**
 * Parameters for a new discount to be added to the DB layer.
 */
public class NewDiscountParams {

    private int storeId;

    private int productId;

    private float originalPrice;

    private float discountedPrice;

    private long expirationDate;

    private int chainId;

    public NewDiscountParams(int storeId, int productId, float originalPrice, float discountedPrice, long expirationDate, int chainId) {
        this.storeId = storeId;
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.expirationDate = expirationDate;
        this.chainId = chainId;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(float discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }
}

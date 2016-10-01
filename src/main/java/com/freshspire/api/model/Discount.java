package com.freshspire.api.model;

import javax.persistence.*;

@Entity
@Table(name="discount")
public class Discount {

    @Id
    @Column(name = "discountId")
    @GeneratedValue
    private int discountId;

    @Column(name = "storeId")
    private int storeId;

    @Column(name = "productId")
    private int productId;

    @Column(name = "posted")
    private long posted;

    @Column(name = "expirationDate")
    private long expirationDate;

    @Column(name = "originalPrice")
    private float originalPrice;

    @Column(name = "discountedPrice")
    private float discountedPrice;

    @Column(name = "chainId")
    private int chainId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit")
    private String unit;

    public Discount() {}

    public Discount(int storeId, int productId, long posted, long expirationDate, float originalPrice,
                    float discountedPrice, int chainId, int quantity, String unit) {
        this.storeId = storeId;
        this.productId = productId;
        this.posted = posted;
        this.expirationDate = expirationDate;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.chainId = chainId;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
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

    public long getPosted() {
        return posted;
    }

    public void setPosted(long posted) {
        this.posted = posted;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
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

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}


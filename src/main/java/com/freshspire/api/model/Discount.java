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

    public Discount() {
    }

    public Discount(int storeId, int productId, long posted, long expirationDate, float originalPrice, float discountedPrice, int chainId) {
        this.storeId = storeId;
        this.productId = productId;
        this.posted = posted;
        this.expirationDate = expirationDate;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.chainId = chainId;
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
}


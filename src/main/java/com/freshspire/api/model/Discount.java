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
    private int posted;

    @Column(name = "expirationDate")
    private int expirationDate;

    @Column(name = "originalPrice")
    private float originalPrice;

    @Column(name = "discountedPrice")
    private float discountedPrice;

    public Discount() {}

    public Discount(int storeId, int productId, int posted, int expirationDate, float originalPrice, float discountedPrice) {
        this.storeId = storeId;
        this.productId = productId;
        this.posted = posted;
        this.expirationDate = expirationDate;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
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

    public int getPosted() {
        return posted;
    }

    public void setPosted(int posted) {
        this.posted = posted;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
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
}

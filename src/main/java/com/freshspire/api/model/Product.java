package com.freshspire.api.model;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {

    @Id
    @Column(name = "productId")
    @GeneratedValue
    private int productId;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "chainId")
    private int chainId;

    @Column(name = "foodType")
    private String foodType;

    /**
     * Stores Product thumbnail URL.
     */
    @Column(name = "thumbnail")
    private String thumbnail;

    public Product() {}

    public Product(String displayName, int chainId, String foodType, String thumbnail) {
        this.displayName = displayName;
        this.chainId = chainId;
        this.foodType = foodType;
        this.thumbnail = thumbnail;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

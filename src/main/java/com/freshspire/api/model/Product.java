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
    private String name;

    @Column(name = "chainId")
    private String chainId;

    @Column(name = "foodType")
    private String foodType;

    public Product() {

    }

    public Product(String name, String chainId, String foodType) {
        this.name = name;
        this.chainId = chainId;
        this.foodType = foodType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}

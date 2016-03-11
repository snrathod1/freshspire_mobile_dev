package com.freshspire.api.model;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {

    @Id
    @Column(name = "productId")
    @GeneratedValue
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}

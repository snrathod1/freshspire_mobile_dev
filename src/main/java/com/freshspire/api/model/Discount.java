package com.freshspire.api.model;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "activeFrom", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activeFrom;

    @Column(name = "activeUntil", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activeUntil;

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

    public Date getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Date getActiveUntil() {
        return activeUntil;
    }

    public void setActiveUntil(Date activeUntil) {
        this.activeUntil = activeUntil;
    }
}

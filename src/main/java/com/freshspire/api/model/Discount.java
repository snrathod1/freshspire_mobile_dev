package com.freshspire.api.model;

import javax.persistence.*;
import java.util.Calendar;
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

    @Column(name = "posted", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date posted;

    @Column(name = "expires", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;

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

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}

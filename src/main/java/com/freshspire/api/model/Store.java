package com.freshspire.api.model;

import javax.persistence.*;

@Entity
@Table(name="store")
public class Store {

    @Id
    @Column(name = "storeId")
    @GeneratedValue
    private int storeId;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    private float latitude;

    @Column(name = "long")
    private float longitude;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.storeId + " " + this.address + " " + this.latitude + " : " + this.longitude;
    }
}

package com.freshspire.api.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="stores")
public class Store {

    @Id
    @Column(name = "storeId")
    @GeneratedValue
    private int storeId;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "zipCode")
    private String zipCode;

    public Store() {}

    public Store(String address, double latitude, double longitude, String zipCode) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipCode = zipCode;
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return this.storeId + " " + this.address + " " + this.latitude + " : " + this.longitude;
    }
}

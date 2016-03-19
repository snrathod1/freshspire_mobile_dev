package com.freshspire.api.model;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;

import javax.persistence.*;

@Spatial
@Indexed
@Entity
@Table(name="store")
public class Store {

    @Id
    @Column(name = "storeId")
    @GeneratedValue
    private int storeId;

    @Column(name = "address")
    private String address;

    @Latitude
    @Column(name = "lat")
    private double latitude;

    @Longitude
    @Column(name = "long")
    private double longitude;

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

    @Override
    public String toString() {
        return this.storeId + " " + this.address + " " + this.latitude + " : " + this.longitude;
    }
}

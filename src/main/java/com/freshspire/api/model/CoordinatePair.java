package com.freshspire.api.model;

/**
 * A latitude/longitude coordinate pair. Latitude and longitude stored as floats.
 */
public class CoordinatePair {

    private float latitude;

    private float longitude;

    public CoordinatePair(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}

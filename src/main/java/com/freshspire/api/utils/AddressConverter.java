package com.freshspire.api.utils;

import com.freshspire.api.model.CoordinatePair;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Converts a US street address to a latitude/longitude CoordinatePair.
 */
public class AddressConverter {

    private static final Gson gson = new Gson();

    private static final String BASE_URL = "https://api.geocod.io/v1/geocode";

    private static final String API_KEY = "1aebe1b751a11aa757f966b1fa86cc53b1e0fac";
    /**
     * Converts an address to the latitude and longitude of that address (represented by the CoordinatePair class).
     * @param address The address to get the lat/long pair from
     * @return The latitude and longitude of `address` as a CoordinatePair
     */
    public static CoordinatePair getLatLongFromAddress(String address) throws IllegalArgumentException {
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL)
                    .queryString("api_key", API_KEY)
                    .queryString("q", address)
                    .asString();

            JsonObject body = gson.fromJson(response.getBody(), JsonObject.class);
            JsonObject location = body.getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonObject("location");

            float latitude = location.get("lat").getAsFloat();
            float longitude = location.get("lng").getAsFloat();

            return new CoordinatePair(latitude, longitude);

        } catch(UnirestException e) {
            throw new IllegalArgumentException("Error when converting address to lat/long");
        }
    }
}

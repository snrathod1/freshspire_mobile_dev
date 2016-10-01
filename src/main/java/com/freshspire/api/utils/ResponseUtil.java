package com.freshspire.api.utils;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Product;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by aadisriram on 2/20/16.
 */
public class ResponseUtil {

    private static Gson gson = new Gson();
    
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String OK = "ok";
    private static final String MESSAGE = "message";

    public static JSONObject getStatusResponseString(String message, String status) {
        JSONObject statusObject = new JSONObject();
        statusObject.put(MESSAGE, message);
        statusObject.put(STATUS, status);
        return statusObject;
    }

    public static String asJsonString(Object object, Class userClass) {
        return gson.toJson(object, userClass);
    }

    public static JSONObject addToJson(String label, JSONObject jsonObject, Object object) {
        jsonObject.put(label, object);
        return jsonObject;
    }

    /**
     * Returns a 400 Bad Request with status: error and message: (message) properties in JSON in the body
     * @param message Error message to include
     * @return A 400 Bad Request response with the JSON information included in the body
     */
    public static ResponseEntity<String> badRequest(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, ERROR);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.badRequest().body(json.toString());
    }

    /**
     * Returns a 200 OK with status: ok and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 200 OK response with the JSON information included in the body
     */
    public static ResponseEntity<String> ok(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, OK);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.ok(json.toString());
    }

    /**
     * Returns a 401 Unauthorized with status: error and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 401 Unauthorized response with the JSON information included in the body
     */
    public static ResponseEntity<String> unauthorized(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, ERROR);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(json));
    }

    /**
     * Returns a 500 Internal Server Error with status: error and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 500 Internal Server Error response with the JSON information included in the body
     */
    public static ResponseEntity<String> serverError(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, ERROR);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(json));
    }

    /**
     * Returns HTTP response with a user object in the response body as JSON, but the user object
     * only includes attributes of the user that need to be included in the response.
     * For example, "firstName" is included but "salt" isn't (and shouldn't be).
     * @param user The User object to put in the response
     * @param status The desired HTTP status of the response
     * @return The HTTP response with the pared-down User object
     */
    public static ResponseEntity<String> makeUserObjectResponse(User user, HttpStatus status) {
        JsonObject userJson = new JsonObject();

        userJson.addProperty("apiKey", user.getApiKey());
        userJson.addProperty("enabledLocation", user.getEnabledLocation());
        userJson.addProperty("firstName", user.getFirstName());
        userJson.addProperty("phoneNumber", user.getPhoneNumber());
        userJson.addProperty("userId", user.getUserId());

        return ResponseEntity.status(status).body(userJson.toString());
    }

    /**
     * Returns HTTP response with a store object in the response body formatted as JSON according
     * to the API specification.
     * @param store The store object to put in the response
     * @param status The desired HTTP status of the response
     * @return The HTTP response with the store JSON object in the response body
     */
    public static ResponseEntity<String> makeStoreObjectResponse(Store store, HttpStatus status) {
        JsonObject storeJson = new JsonObject();

        storeJson.addProperty("storeId", store.getStoreId());
        storeJson.addProperty("chainId", store.getChainId());
        storeJson.addProperty("displayName", store.getDisplayName());
        storeJson.addProperty("street", store.getStreet());
        storeJson.addProperty("city", store.getCity());
        storeJson.addProperty("state", store.getState());
        storeJson.addProperty("zipCode", store.getZipCode());
        storeJson.addProperty("latitude", store.getLatitude());
        storeJson.addProperty("longitude", store.getLongitude());

        return ResponseEntity.status(status).body(storeJson.toString());
    }

    /**
     * Returns a 404 Not Found with status: error and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 404 Not Found response with the JSON information included in the body
     */
    public static ResponseEntity<String> notFound(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, ERROR);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(json));
    }

    /**
     * Returns HTTP response with a product object in the response body formatted as JSON according
     * to the API specification.
     * @param product The product object to put in the response
     * @param status The desired HTTP status of the response
     * @return The HTTP response with the product JSON object in the response body
     */
    public static ResponseEntity<String> makeProductObjectResponse(Product product, HttpStatus status) {
        JsonObject json = new JsonObject();
        json.addProperty("productId", product.getProductId());
        json.addProperty("chainId", product.getChainId());
        json.addProperty("displayName", product.getDisplayName());
        json.addProperty("foodType", product.getFoodType());

        return ResponseEntity.status(status).body(json.toString());
    }

    public static ResponseEntity<String> makeDiscountObjectResponse(Discount discount, HttpStatus status) {
        JsonObject json = new JsonObject();
        json.addProperty("discountId", discount.getDiscountId());
        json.addProperty("originalPrice", discount.getOriginalPrice());
        json.addProperty("discountedPrice", discount.getDiscountedPrice());
        json.addProperty("quantity", discount.getQuantity());
        json.addProperty("unit", discount.getUnit());

        return ResponseEntity.status(status).body(json.toString());
    }
}

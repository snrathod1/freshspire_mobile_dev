package com.freshspire.api.utils;

import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.regexp.internal.RE;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sun.misc.REException;

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
     * @return A 401 Unauthorized response with the JSON information include in the body
     */
    public static ResponseEntity<String> unauthorized(String message) {
        JsonObject json = new JsonObject();
        json.addProperty(STATUS, ERROR);
        json.addProperty(MESSAGE, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(json));
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
        userJson.addProperty("firstName", user.getFirstName());
        userJson.addProperty("phoneNumber", user.getPhoneNumber());
        if(user.getUserId() == null) {
            userJson.addProperty("userId", "nuuuuullll");
        } else { userJson.addProperty("userId", user.getUserId()); }


        return ResponseEntity.status(status).body(gson.toJson(userJson));
    }
}

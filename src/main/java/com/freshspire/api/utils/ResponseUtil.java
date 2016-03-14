package com.freshspire.api.utils;

import com.freshspire.api.model.ResponseMessage;
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

    public static JSONObject getStatusResponseString(String message, String status) {
        JSONObject statusObject = new JSONObject();
        statusObject.put("message", message);
        statusObject.put("status", status);
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
        json.addProperty("status", "error");
        json.addProperty("message", message);
        return ResponseEntity.badRequest().body(gson.toJson(json));
    }

    /**
     * Returns a 200 OK with status: ok and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 200 OK response with the JSON information included in the body
     */
    public static ResponseEntity<String> ok(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("status", "ok");
        json.addProperty("message", message);
        return ResponseEntity.ok().body(gson.toJson(json));
    }

    /**
     * Returns a 401 Unauthorized with status: error and message: (message) properties in JSON in the body
     * @param message Message to include
     * @return A 401 Unauthorized response with the JSON information include in the body
     */
    public static ResponseEntity<String> unauthorized(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("status", "error");
        json.addProperty("message", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(json));
    }
}

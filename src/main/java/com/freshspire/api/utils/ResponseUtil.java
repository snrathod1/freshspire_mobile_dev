package com.freshspire.api.utils;

import com.freshspire.api.model.ResponseMessage;
import com.google.gson.Gson;
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
     * Returns a ResponseEntity with status 400 Bad Request and a response body of res in JSON format
     * @param res The desired response content (such as an error message "status": "error", "message": "...")
     * @return A 400 Bad Request response with the json representation of res in the body
     */
    public static <E> ResponseEntity<String> badRequest(E res) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asJsonString(res, res.getClass()));
    }

    /**
     * Returns a ResponseEntity with status 200 OK and a response body of res in JSON format
     * @param res The desired response content (such as an ok message "status": "ok", "message": "...")
     * @return A 200 OK response with the json representation of res in the body
     */
    public static <E> ResponseEntity<String> ok(E res) {
        return ResponseEntity.ok().body(asJsonString(res, res.getClass()));
    }
}

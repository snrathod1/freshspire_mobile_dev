package com.freshspire.api.utils;

import com.google.gson.Gson;
import org.json.JSONObject;

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
}

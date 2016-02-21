package com.freshspire.api.utils;

import com.google.gson.Gson;
import org.json.JSONObject;

/**
 * Created by aadisriram on 2/20/16.
 */
public class ResponseUtil {

    private static Gson gson = new Gson();

    public static JSONObject getStatusResponseString(String message, boolean success) {
        JSONObject statusData = new JSONObject();
        statusData.put("message", message);
        statusData.put("success", success);

        JSONObject status = new JSONObject();
        status.put("status", statusData);

        return status;
    }

    public static String getDataJson(Object object, Class userClass) {
        return gson.toJson(object, userClass);
    }

    public static JSONObject addToJson(String label, JSONObject jsonObject, Object object) {
        jsonObject.put(label, object);
        return jsonObject;
    }
}

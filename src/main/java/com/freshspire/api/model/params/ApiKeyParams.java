package com.freshspire.api.model.params;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * com.freshspire.api.model
 *
 * @created 2/29/16.
 */
public class ApiKeyParams {

    private String apiKey;

    public ApiKeyParams() {}

    public ApiKeyParams(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

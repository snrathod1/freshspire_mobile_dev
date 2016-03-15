package com.freshspire.api.model.params;

/**
 * com.freshspire.api.model
 *
 * @created 2/29/16.
 */
public class ApiKeyLoginParams {

    private String apiKey;

    public ApiKeyLoginParams(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

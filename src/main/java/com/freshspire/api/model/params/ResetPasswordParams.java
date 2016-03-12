package com.freshspire.api.model.params;

/**
 * Created by aadisriram on 2/25/16.
 */
public class ResetPasswordParams {

    private String currentPassword;
    private String newPassword;
    private String apiKey;

    public ResetPasswordParams(String apiKey, String currentPassword, String newPassword) {
        this.apiKey = apiKey;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

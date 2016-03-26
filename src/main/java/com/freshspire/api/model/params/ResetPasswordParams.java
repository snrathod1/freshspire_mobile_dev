package com.freshspire.api.model.params;

public class ResetPasswordParams {

    private String oldPassword;
    private String newPassword;
    private String apiKey;

    public ResetPasswordParams(String apiKey, String oldPassword, String newPassword) {
        this.apiKey = apiKey;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

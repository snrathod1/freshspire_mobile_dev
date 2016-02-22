package com.freshspire.api.model;


public class TempCredentialsParams {

    // Parameters
    private String phoneNumber;
    private boolean canCreateUser;
    private boolean canUpdatePassword;


    // Methods
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean canCreateUser() {
        return canCreateUser;
    }

    public void isCanCreateUser(boolean canCreateUser) {
        this.canCreateUser = canCreateUser;
    }

    public boolean canUpdatePassword() {
        return canUpdatePassword;
    }

    public void CanUpdatePassword(boolean canUpdatePassword) {
        this.canUpdatePassword = canUpdatePassword;
    }
}

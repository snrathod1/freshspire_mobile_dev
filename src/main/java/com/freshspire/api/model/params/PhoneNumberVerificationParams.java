package com.freshspire.api.model.params;

/**
 * Created by aadisriram on 2/25/16.
 */
public class PhoneNumberVerificationParams {

    private String phoneNumber;

    private String verificationCode;

    private String newPassword;

    public PhoneNumberVerificationParams(String phoneNumber, String verificationCode, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

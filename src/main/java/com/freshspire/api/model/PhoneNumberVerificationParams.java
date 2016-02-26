package com.freshspire.api.model;

/**
 * Created by aadisriram on 2/25/16.
 */
public class PhoneNumberVerificationParams {

    private String phoneNumber;

    private String verificationCode;

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
}

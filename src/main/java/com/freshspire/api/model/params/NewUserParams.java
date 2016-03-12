package com.freshspire.api.model.params;

/**
 * This is the model for the request parameters
 */

public class NewUserParams {

    // Members
    private String firstName;

    private String phoneNumber;

    private String password;

    private String validationCode;

    public NewUserParams(String firstName, String phoneNumber, String password, String validationCode) {
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.validationCode = validationCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }


}

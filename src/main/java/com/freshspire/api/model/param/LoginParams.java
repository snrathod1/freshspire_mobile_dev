package com.freshspire.api.model.param;

/**
 * Request parameters for /users/login
 *
 * @created 2/29/16.
 */
public class LoginParams {

    private String phoneNumber;

    private String password;

    public LoginParams() {}

    public LoginParams(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}

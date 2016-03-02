package com.freshspire.api.model;

/**
 * Request parameters for /users/login
 *
 * @created 2/29/16.
 */
public class LoginParams {

    private String phoneNumber;

    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}

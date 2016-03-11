package com.freshspire.api.client;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.PhoneVerification;
import com.authy.api.Verification;

/**
 * Created by aadisriram on 2/20/16.
 */
public class AuthyClient {

    private static AuthyApiClient client;
    private static Params params;

    private AuthyClient() {

    }

    public AuthyApiClient getClient() {
        if (client == null) {
            client = new AuthyApiClient("H9PXb4awsJtTV2Cza7qAFqMIwIyePefR");
            params = new Params();
            params.setAttribute("locale", "en");
        }

        return client;
    }

    public PhoneVerification getPhoneVerificationClient() {
        return getClient().getPhoneVerification();
    }

    public Verification startAuthentication(String phoneNumber) {
        return getPhoneVerificationClient().start(phoneNumber, "1", "sms", params);
    }

    public Verification checkAuthentication(String phoneNumber, String authCode) {
        return getPhoneVerificationClient().check(phoneNumber, "1", authCode);
    }
}

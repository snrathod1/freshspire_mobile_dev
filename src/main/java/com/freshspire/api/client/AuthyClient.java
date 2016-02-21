package com.freshspire.api.client;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.PhoneVerification;
import com.authy.api.Verification;

/**
 * Created by aadisriram on 2/20/16.
 */
public class AuthyClient {

    private static AuthyApiClient _client;
    private static Params params;

    private AuthyClient() {

    }

    public static AuthyApiClient getClient() {
        if (_client == null) {
            _client = new AuthyApiClient("H9PXb4awsJtTV2Cza7qAFqMIwIyePefR");
            params = new Params();
            params.setAttribute("locale", "en");
        }

        return _client;
    }

    public static PhoneVerification getPhoneVerificationClient() {
        AuthyApiClient client = getClient();
        PhoneVerification phoneVerification = client.getPhoneVerification();
        return phoneVerification;
    }

    public static Verification startAuthentication(String phoneNumber) {
        return getPhoneVerificationClient().start(phoneNumber, "1", "sms", params);
    }

    public static Verification checkAuthentication(String phoneNumber, String authCode) {
        return getPhoneVerificationClient().check(phoneNumber, "1", authCode);
    }
}

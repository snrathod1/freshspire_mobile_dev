package com.freshspire.api.client;

import com.authy.AuthyApiClient;
import com.authy.api.PhoneVerification;

/**
 * Created by aadisriram on 2/20/16.
 */
public class AuthyClient {

    private static AuthyApiClient _client;

    private AuthyClient() {

    }

    public static AuthyApiClient getClient() {
        if (_client == null) {
            _client = new AuthyApiClient("H9PXb4awsJtTV2Cza7qAFqMIwIyePefR");
        }

        return _client;
    }

    public static PhoneVerification getVerificationClient() {
        AuthyApiClient client = getClient();
        PhoneVerification phoneVerification = client.getPhoneVerification();

        return phoneVerification;
    }
}

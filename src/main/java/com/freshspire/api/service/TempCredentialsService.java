package com.freshspire.api.service;

import com.freshspire.api.model.TempCredentials;

public interface TempCredentialsService {
    void addTempCredentials(TempCredentials credentials);
    void updateTempCredentials(TempCredentials credentials);
    TempCredentials getTempCredentials(String phoneNumber);

}

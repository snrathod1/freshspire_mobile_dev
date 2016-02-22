package com.freshspire.api.dao;

import com.freshspire.api.model.TempCredentials;

public interface TempCredentialsDAO {

    void addTempCredentials(TempCredentials creds);
    void updateTempCredentials(TempCredentials creds);
    TempCredentials getTempCredentials(String phoneNumber);

}

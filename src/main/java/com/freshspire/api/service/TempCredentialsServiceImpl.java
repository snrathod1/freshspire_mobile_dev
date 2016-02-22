package com.freshspire.api.service;

import com.freshspire.api.dao.TempCredentialsDAO;
import com.freshspire.api.model.TempCredentials;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by mitch on 2/21/16.
 */
public class TempCredentialsServiceImpl implements TempCredentialsService {

    private TempCredentialsDAO tempCredentialsDAO;

    public void setTempCredentialsDAO(TempCredentialsDAO tempCredentialsDAO) {
        this.tempCredentialsDAO = tempCredentialsDAO;
    }

    @Transactional
    public void addTempCredentials(TempCredentials credentials) {
        this.tempCredentialsDAO.addTempCredentials(credentials);
    }

    @Transactional
    public void updateTempCredentials(TempCredentials credentials) {
        this.tempCredentialsDAO.updateTempCredentials(credentials);
    }

    @Transactional
    public TempCredentials getTempCredentials(String phoneNumber) {
        return this.tempCredentialsDAO.getTempCredentials(phoneNumber);
    }
}

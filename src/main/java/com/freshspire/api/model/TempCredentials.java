package com.freshspire.api.model;

import org.hibernate.annotations.Type;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tempCredentials")
public class TempCredentials {

    @Id
    @Column(name = "tempApiKey")
    private String tempApiKey;

    @Column(name = "created")
    private Date created;

    @Column(name="canCreateUser", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean canCreateUser;

    @Column(name="canUpdatePassword", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean canUpdatePassword;

    @Column(name = "confirmationCode")
    private String confirmationCode;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    public String getTempApiKey() {
        return tempApiKey;
    }

    public void setTempApiKey(String tempApiKey) {
        this.tempApiKey = tempApiKey;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isCanCreateUser() {
        return canCreateUser;
    }

    public void setCanCreateUser(boolean canCreateUser) {
        this.canCreateUser = canCreateUser;
    }

    public boolean isCanUpdatePassword() {
        return canUpdatePassword;
    }

    public void setCanUpdatePassword(boolean canUpdatePassword) {
        this.canUpdatePassword = canUpdatePassword;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

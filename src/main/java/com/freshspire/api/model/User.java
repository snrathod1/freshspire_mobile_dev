package com.freshspire.api.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="userId")
    @GeneratedValue
    @Expose
    private int userId;

    @Column(name="firstName")
    @Expose
    private String firstName;

    @Column(name="phoneNumber")
    @Expose
    private String phoneNumber;

    @Column(name="apiKey")
    @Expose
    private String apiKey;

    @Column(name="password")
    private String password;

    @Column(name="salt")
    private String salt;

    @Column(name="created", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name="admin", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean admin;

    @Column(name="banned", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean banned;

    @Column(name="enabledLocation", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Expose
    private boolean enabledLocation;

    public User() {}

    public User(String firstName, String phoneNumber, String apiKey, String password, String salt,
                Date created, boolean admin, boolean banned, boolean enabledLocation) {
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.apiKey = apiKey;
        this.password = password;
        this.salt = salt;
        this.created = created;
        this.admin = admin;
        this.banned = banned;
        this.enabledLocation = enabledLocation;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean getEnabledLocation() {
        return enabledLocation;
    }

    public void setEnabledLocation(boolean enabledLocation) {
        this.enabledLocation = enabledLocation;
    }

    @Override
    public String toString(){
        return "id="+userId+", name="+firstName;
    }
}

package com.freshspire.api;

import java.util.Date;

public final class TestConstants {

    public static final int VALID_USER_ID                   = 1;
    public static final int INVALID_USER_ID                 = 1001;
    
    public static final int VALID_STORE_ID                  = 2;
    public static final int INVALID_STORE_ID                = 1002;
    
    public static final int VALID_CHAIN_ID                  = 3;
    public static final int INVALID_CHAIN_ID                = 1003;
    
    public static final String VALID_API_KEY                = "validApiKey";
    public static final String INVALID_API_KEY              = "invalid API key";

    public static final String VALID_FIRST_NAME             = "FirstName";
    public static final String INVALID_FIRST_NAME           = "invalidFirstName";

    public static final String VALID_PASSWORD               = "A valid password";
    public static final String INVALID_PASSWORD             = "An invalid password";

    public static final String VALID_SALT                   = "validSalt";
    public static final String INVALID_SALT                 = "invalidSalt";

    public static final String VALID_PHONE_NUMBER           = "1234567890";
    public static final String INVALID_PHONE_NUMBER         = "invalidPho";

    public static final String VALID_AUTHENTICATION_CODE    = "0007";
    public static final String INVALID_AUTHENTICATION_CODE  = "nope";

    public static final boolean VALID_ENABLED_LOCATION      = false;
    public static final boolean INVALID_ENABLED_LOCATION    = true;

    public static final boolean VALID_ADMIN                 = false;
    public static final boolean INVALID_ADMIN               = true;

    public static final boolean VALID_BANNED                = false;
    public static final boolean INVALID_BANNED              = true;

    public static final Date VALID_DATE                     = new Date(0);
    public static final Date INVALID_DATE                   = new Date(1000);
    
    public static final String VALID_CITY                   = "Raleigh";
    public static final String INVALID_CITY                 = "not a city";

    public static final String VALID_DISPLAY_NAME           = "Cameron Village Harris Teeter";
    public static final String INVALID_DISPLAY_NAME         = "Invalid display name";
    
    public static final String VALID_STREET                 = "500 Oberlin Road";
    public static final String INVALID_STREET               = "not a valid street address";

    public static final String VALID_STATE                  = "NC";
    public static final String INVALID_STATE                = "XX";
    
    public static final String VALID_ZIP_CODE               = "27605";
    public static final String INVALID_ZIP_CODE             = "this is a bad zip code";

    public static final double VALID_LATITUDE               = 35.7889461;
    public static final double INVALID_LATITUDE             = 999.99;

    public static final double VALID_LONGITUDE              = -78.6623816;
    public static final double INVALID_LONGITUDE            = 888.88;
}

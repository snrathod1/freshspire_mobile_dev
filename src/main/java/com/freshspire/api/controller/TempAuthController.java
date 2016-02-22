package com.freshspire.api.controller;

import com.authy.api.PhoneVerification;
import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.TempCredentials;
import com.freshspire.api.model.TempCredentialsParams;
import com.freshspire.api.service.TempCredentialsService;
import com.freshspire.api.utils.PasswordUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/temp-auth")
public class TempAuthController {

    // TODO this service breaks when used
    @Autowired
    private TempCredentialsService tempCredentialsService;

    /**
     * POST /temp-auth
     *
     * This creates temporary authentication credentials for a phone number.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String createTempAuthCreds(@RequestBody TempCredentialsParams params) {

        // creates temporary authentication credentials that have a specific permission (create a user, update a password, etc)
        TempCredentials tempCreds = new TempCredentials();

        // These fields are specified in the parameters
        tempCreds.setPhoneNumber(params.getPhoneNumber());
        tempCreds.setCanCreateUser(params.canCreateUser());
        tempCreds.setCanUpdatePassword(params.canUpdatePassword());

        // These fields are generated
        tempCreds.setCreated(new Date());
        tempCreds.setTempApiKey(PasswordUtil.generateApiKey());

        // Put it in the tempAuthCredentials table
        tempCredentialsService.addTempCredentials(tempCreds);

        Verification verification = AuthyClient.startAuthentication(params.getPhoneNumber());

        return new JSONObject()
                .append("message", verification.getMessage())
                .toString();
    }

    /**
     * GET /temp-auth
     *
     * This gets the API key of {phoneNumber}'s temp credentials which the client can use to do the action it needs
     * (i.e. create user, modify password, etc.)
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String getTempAuthCreds(@RequestParam String phoneNumber,
                                   String confirmationCode) {

        PhoneVerification phoneVerification = AuthyClient.getPhoneVerificationClient();

        // TODO we should probably have the user store/specify country code (hard coded right now as "1")
        Verification verification = phoneVerification.check(phoneNumber, "1", confirmationCode);

        if(verification.isOk()) {
            // return the API key with 200 OK
            return new JSONObject()
                    .append("message", "your code is CORRECT. this will eventually return the temp api key.")
                    .toString();
        } else {
            // return error with 4xx response
            return new JSONObject()
                    .append("message", "your code is INCORRECT or ALREADY USED. this will eventually return the temp api key.")
                    .toString();
        }
        // TODO return api key instead of debug message
    }
}

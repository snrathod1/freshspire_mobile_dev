package com.freshspire.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mitch on 2/21/16.
 */
@RestController
@RequestMapping("/temp-auth")
public class TempAuthController {

    /**
     * POST /temp-auth
     *
     * This creates temporary authentication credentials for a phone number.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String createTempAuthCreds() {
        // creates temporary authentication credentials that have a specific permission (create a user, update a password, etc)
        // TODO add phoneNumber param, permissionType param
        return "{ \"message\": \"not implemented\" }";
    }

    /**
     * GET /temp-auth/{phoneNumber}
     *
     * This gets the API key of {phoneNumber}'s temp credentials which the client can use to do the action it needs
     * (i.e. create user, modify password, etc.)
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String getTempAuthCreds(@PathVariable String phoneNumber) {
        // TODO add confirmationCode param
        return "{ \"message\": \"not implemented\" }";
    }
}

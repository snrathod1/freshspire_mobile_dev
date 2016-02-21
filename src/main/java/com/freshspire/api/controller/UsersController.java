package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.NewUserParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by reesjones on 2/13/16.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    /**
     * GET /users
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String getUsers() {
        return "{ \"message\": \"You're at the /users controller. Nothing here!\" }";
    }

    /**
     * POST /users
     *
     * This creates a user. There are two requests required; the first has a phone number and password,
     * and sending that request sends a code to the phone number provided. The second request includes the phone
     * number, password, AND the confirmation code. Once this second request is received, the user is created and the
     * user object is returned.
     *
     * TODO validate firstName, phoneNumber, password, and apiKey before doing anything
     * TODO when validating API key, should it only check temp_auth_creds table or also users table? Would an admin create a user?
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String createUser(@RequestBody NewUserParams userParams) {

        // TODO Hibernate or Spring doesn't know what NewUserParams is ^^^
        String password = userParams.getPassword();

        // Make a new user object and populate its fields
        User newUser = new User();
        newUser.setPassword(userParams.getPassword());
        newUser.setApiKey(userParams.getApiKey());
        newUser.setCreatedOn(new Date());
        newUser.setSalt(PasswordUtil.generateSaltString());
        newUser.setPassword(PasswordUtil.encryptString(password, newUser.getSalt()));

        // Modify user object a bit before it's put in the response
        newUser.setPassword(userParams.getPassword()); // TODO need this?
        newUser.setSalt(null); // TODO remove this field completely; try http://stackoverflow.com/questions/4802887

        return ResponseUtil.asJsonString(newUser, User.class);
    }

    /**
     * TODO need this?
     *
     * GET /users/auth/{userId}/{password}
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "/auth/{userId}/{password}", method = RequestMethod.GET, produces = "application/json")
    public String authUser(@PathVariable String userId, @PathVariable String password) {
        User user = userService.getUserById(userId);
        return ResponseUtil.getStatusResponseString(
                "Authenticating User",
                PasswordUtil.matchPassword(password, user.getSalt(), user.getPassword()) ? "ok" : "error"
        ).toString();
    }

    /**
     * TODO need this?
     *
     * GET /users/create/{phoneNumber}
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/create/{phoneNumber}", method = RequestMethod.GET, produces = "application/json")
    public String createUserWithPhone(@PathVariable String phoneNumber) {
        Verification verification = AuthyClient.startAuthentication(phoneNumber);
        return ResponseUtil.getStatusResponseString(verification.getMessage(), verification.isOk() ? "ok" : "error").toString();
    }

    /**
     * TODO need this?
     *
     * GET /users/verify/{phoneNumber}/{authCode}
     * @param phoneNumber
     * @param authCode
     * @return
     */
    @RequestMapping(value = "/verify/{phoneNumber}/{authCode}", method = RequestMethod.GET, produces = "application/json")
    public String verifyNumber(@PathVariable String phoneNumber, @PathVariable String authCode) {
        Verification verification = AuthyClient.checkAuthentication(phoneNumber, authCode);
        return ResponseUtil.getStatusResponseString(verification.getMessage(), verification.isOk() ? "ok" : "error").toString();
    }

    /**
     * GET /users/{userId}
     *
     * TODO handle bad request (api key invalid, etc.)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = "application/json")
    public String getUserById(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        user.setPassword(null); // TODO remove field altogether
        user.setSalt(null); // TODO probably should remove this altogether as well
        return gson.toJson(userService.getUserById(userId));
    }
}
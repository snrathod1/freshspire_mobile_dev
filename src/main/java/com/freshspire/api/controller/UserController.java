package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.response.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.param.*;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Handles all methods under /users endpoint, except login endpoints (/users/login and /users/key-login)
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /** Service layer for Users in database */
    private UserService userService;

    /** Client instance for sending & authenticating text messages with Authy */
    private AuthyClient authyClient;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthyClient(AuthyClient authyClient) {
        this.authyClient = authyClient;
    }

    /**
     * GET /users/debug
     */
    @RequestMapping(value = "/debug", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getUsers() {
        JsonObject obj = new JsonObject();
        obj.addProperty("status", "ok");
        obj.addProperty("message", "debug message");
        ResponseMessage res = new ResponseMessage("ok", "Users endpoint is up. jsonObject is:\n" + gson.toJson(obj));
        return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
    }

    /**
     * GET /users/create
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getNewUserAuthCode(@RequestParam String phoneNumber) {

        // If phone is null
        if(phoneNumber == null) {
            return ResponseUtil.badRequest("Phone number is invalid");
        } else if(phoneNumber.length() == 0) {
            return ResponseUtil.badRequest("Phone number is empty");
        }

        if(userService.getUserByPhoneNumber(phoneNumber) != null) {
            return ResponseUtil.badRequest("User already exists for phone number " + phoneNumber);
        }

        Verification verification = authyClient.startAuthentication(phoneNumber);

        if(!verification.isOk()) {
            return ResponseUtil.badRequest("Could not send authentication code. Is phone number formatted correctly?");
        }

        return ResponseUtil.ok("Authentication code sent to " + phoneNumber);
    }

    /**
     * POST /users/create
     *
     * This creates a user. There are two requests required (this is the first one); the first has a
     * phone number and password, and sending that request sends a code to the phone number provided.
     * The second request includes the phone number, password, AND the confirmation code. Once this
     * second request is received, the user is created and the user object is returned.
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> createUser(@RequestBody NewUserParams params) {

        String code = params.getValidationCode();

        if(code.length() == 0) {
            return ResponseUtil.unauthorized("Invalid phone number/authentication code pair");
        } else if(params.getPassword().length() == 0) {
            return ResponseUtil.badRequest("Password parameter cannot be empty");
        } else if(params.getFirstName().length() == 0) {
            return ResponseUtil.badRequest("First name parameter cannot be empty");
        }

        if(userService.getUserByPhoneNumber(params.getPhoneNumber()) != null) {
            return ResponseUtil.badRequest("User already exists for phone number " + params.getPhoneNumber());
        }

        Verification verification = authyClient.checkAuthentication(params.getPhoneNumber(), code);

        if(!verification.isOk())
            return ResponseUtil.unauthorized("Invalid phone number/authentication code pair");

        // Verification was good, so now validate new user param and then create the user
        if(params.getPassword() == null
                || params.getFirstName() == null
                || params.getPhoneNumber() == null) {
            return ResponseUtil.badRequest("Invalid request parameters");
        }

        // New user param were valid, so now create user object, insert into DB, and return it.
        String password = params.getPassword();

        String salt = PasswordUtil.generateSaltString();

        // Make a new user object and populate its fields
        User newUser = new User(params.getFirstName(),
                params.getPhoneNumber(),
                PasswordUtil.generateApiKey(),
                PasswordUtil.encryptString(password, salt),
                salt,
                new Date(),
                false,
                false,
                false);

        userService.addUser(newUser);

        return ResponseUtil.makeUserObjectResponse(newUser, HttpStatus.CREATED);
    }

    /**
     * GET /users/forgot-password/{phoneNumber}
     *
     * Sends a message to the phone number to start the password reset process
     * @param phoneNumber Phone of the user needing password update
     * @return
     */
    @RequestMapping(value = "/forgot-password/{phoneNumber}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> sendCodeForForgotPassword(@PathVariable String phoneNumber) {
        if (userService.getUserByPhoneNumber(phoneNumber) != null) {
            // Then the user exists in the DB for that phone number
            authyClient.startAuthentication(phoneNumber);

        }
        return ResponseUtil.ok("Authentication code sent to " + phoneNumber
                + " if account exists with that number");
    }

    /**
     * PUT /users/forgot-password/
     *
     * Verify the code and set the new password
     * @param params
     * @return user json with status message
     */
    @RequestMapping(value = "/forgot-password", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> verifyCodeForForgotPassword(@RequestBody PhoneNumberAuthenticationParams params) {
        // New password cannot be empty string
        if(params.getNewPassword().length() == 0) {
            return ResponseUtil.badRequest("New password cannot be empty");
        }

        // Check with Authy if the code was correct
        Verification verification = authyClient.checkAuthentication(
                params.getPhoneNumber(),
                params.getVerificationCode());

        // If it is...
        if (verification.isOk()) {
            // Set the password of the user associated with the given phone number
            User user = userService.getUserByPhoneNumber(params.getPhoneNumber());
            String hashedPassword = PasswordUtil.encryptString(params.getNewPassword(), user.getSalt());
            user.setPassword(hashedPassword);
            userService.updateUser(user);

            return ResponseUtil.makeUserObjectResponse(user, HttpStatus.OK);
        } else {
            return ResponseUtil.unauthorized("Phone/authentication code pair is incorrect");
        }
    }

    /**
     * POST /reset-password
     *
     * Reset password for a user by providing the old and new password, and API key associated with the user.
     * @param params
     * @return status of password reset
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordParams params, @RequestHeader("Authorization") String apiKey) {
        // New password can't be empty
        if(params.getNewPassword().length() == 0) {
            return ResponseUtil.badRequest("New password cannot be empty");
        }

        // If the API key doesn't match any user, so return 401 Unauthorized
        User user = userService.getUserByApiKey(apiKey);
        if(user == null) return ResponseUtil.unauthorized("Invalid authentication credentials");

        // If the supplied password matches the user's password, then update it
        if (PasswordUtil.isCorrectPasswordForUser(user, params.getOldPassword())) {
            user.setPassword(PasswordUtil.encryptString(params.getNewPassword(), user.getSalt()));
            userService.updateUser(user);

            return ResponseUtil.ok("Successfully updated password");
        } else {
            // Given password was incorrect, so return 401 Unauthorized
            return ResponseUtil.unauthorized("Invalid authentication credentials");
        }
    }

    /**
     * DELETE /users/{userId}
     *
     * Deletes user with user ID "userId". Body must contain API key of the user.
     * @param userId User's unique ID
     * @param apiKey The user's API key
     * @return Success or error message for bad or unauthenticated request
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId, @RequestHeader("Authorization") String apiKey) {
        // If API key is empty, return error
        if(apiKey.length() == 0)
            return ResponseUtil.unauthorized("User ID/API key pair incorrect");

        User user = userService.getUserById(userId);
        // If user doesn't exist in DB, return error
        if(user == null) return ResponseUtil.unauthorized("User ID/API key pair incorrect");

        // If API key is valid, delete user
        if(user.getApiKey().equals(apiKey)) {
            userService.deleteUser(userId);
            return ResponseUtil.ok("Successfully deleted user");

        } else { // Otherwise, return error
            return ResponseUtil.unauthorized("User ID/API key pair incorrect");
        }
    }

    /**
     * GET /users/{userId}/enabledLocation
     *
     * Returns a user's enabledLocation property. enabledLocation is a boolean value that specifies if the user
     * has enabled location services on their mobile device.
     * @param userId User's unique ID
     * @param apiKey User's API key
     * @return The user's enabledLocation value, or an error message for bad or unauthenticated request
     */
    @RequestMapping(value = "/{userId}/enabledLocation", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getEnabledLocation(@PathVariable("userId") int userId, @RequestHeader("Authorization") String apiKey) {
        // If API key is empty, return error
        if(apiKey.length() == 0)
            return ResponseUtil.badRequest("API key cannot be empty");

        // Find user
        User user = userService.getUserById(userId);
        if(user == null) return ResponseUtil.notFound("User not found");

        // If API key is correct, then user is authenticated, so return enabledLocation
        if(user.getApiKey().equals(apiKey)) {
            JsonObject json = new JsonObject();
            json.addProperty("enabledLocation", user.getEnabledLocation());
            return ResponseEntity.ok(gson.toJson(json));
        } else {
            // API key incorrect, return error
            return ResponseUtil.unauthorized("User ID/API key pair incorrect");
        }
    }

    /**
     * PUT /users/{userId}/enabledLocation
     *
     * Updates a user's enabledLocation property. enabledLocation is a boolean value that specifies if the user
     * has enabled location services on their mobile device.
     * @param userId User's unique ID
     * @param apiKey User's API key
     * @param params Request body parameters (contains enabledLocation parameter)
     * @return Status and message indicating if enabledLocation was successfully updated
     */
    @RequestMapping(value = "/{userId}/enabledLocation", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> updateEnabledLocation(@PathVariable("userId") int userId,
                                                        @RequestHeader("Authorization") String apiKey,
                                                        @RequestBody SetEnabledLocationParams params) {
        // If API key is empty, return error
        if(apiKey.length() == 0)
            return ResponseUtil.badRequest("API key cannot be empty");

        // Find user
        User user = userService.getUserById(userId);
        if(user == null) return ResponseUtil.notFound("User not found");

        // If API key is correct, then user is authenticated, so update enabledLocation
        if(user.getApiKey().equals(apiKey)) {
            user.setEnabledLocation(params.getEnabledLocation());
            userService.updateUser(user);

            return ResponseUtil.ok("Successfully updated enabledLocation");
        } else {
            // API key incorrect, return error
            return ResponseUtil.unauthorized("User ID/API key pair incorrect");
        }
    }
}
package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.params.NewUserParams;
import com.freshspire.api.model.params.PhoneNumberAuthenticationParams;
import com.freshspire.api.model.params.ResetPasswordParams;
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

@RestController
@RequestMapping("/users")
public class UsersController {

    private UserService userService;

    private AuthyClient authyClient;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

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
            ResponseMessage error = new ResponseMessage("error", "Phone number is invalid");
            return ResponseUtil.badRequest(error);
        } else if(phoneNumber.length() == 0) {
            ResponseMessage error = new ResponseMessage("error", "Phone number is empty");
            return ResponseUtil.badRequest(error);
        }

        Verification verification = authyClient.startAuthentication(phoneNumber);

        if(!verification.isOk()) {
            ResponseMessage error = new ResponseMessage("error",
                    "Could not send authentication code. Is phone number formatted correctly?");
            return ResponseUtil.badRequest(error);
        }

        ResponseMessage res = new ResponseMessage("ok", "Authentication code sent to " + phoneNumber);
        return ResponseUtil.ok(res);
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
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Authentication code is empty");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(res));
        }

        Verification verification = authyClient.checkAuthentication(params.getPhoneNumber(), code);

        if(!verification.isOk()) {
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Invalid phone number/authentication code pair");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(res));
        }

        // Verification was good, so now validate new user params and then create the user
        if(params.getPassword() == null
                || params.getPassword().length() == 0
                || params.getFirstName() == null
                || params.getPhoneNumber() == null) {
            ResponseMessage res = new ResponseMessage("error", "Invalid request parameters");
            return ResponseUtil.badRequest(res);
        }

        // New user params were valid, so now create user object, insert into DB, and return it.
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
                false);

        userService.addUser(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(newUser));
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
        if (userService.userExistsWithPhoneNumber(phoneNumber)) {
            // Then the user exists in the DB for that phone number
            authyClient.startAuthentication(phoneNumber);

        }
        return ResponseUtil.ok("Authentication code sent to " + phoneNumber
                + " if account exists with that number");
    }

    /**
     * PUT /users/forgot-password/
     *
     * Verify the code and set the user to restricted mode if verification passes, this enables
     * users to reset password without providing the old password.
     * @param params
     * @return user json with status message
     */
    @RequestMapping(value = "/forgot-password", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> verifyCodeForForgotPassword(@RequestBody PhoneNumberAuthenticationParams params) {
        // New password cannot be empty string
        if(params.getNewPassword().length() == 0) {
            return ResponseUtil.badRequest(new ResponseMessage("error", "New password cannot be empty"));
        }

        // Check with Authy if the code was correct
        Verification verification = authyClient.checkAuthentication(
                params.getPhoneNumber(),
                params.getVerificationCode());

        // If it is...
        if (verification.isOk()) {
            // Set the user associated with the phone number to restricted: true
            User user = userService.getUserByPhoneNumber(params.getPhoneNumber());
            String hashedPassword = PasswordUtil.encryptString(params.getNewPassword(), user.getSalt());
            user.setPassword(hashedPassword);
            userService.updateUser(user);

            JsonObject updatedUser = new JsonObject();
            updatedUser.addProperty("userId", user.getUserId());
            updatedUser.addProperty("apiKey", user.getApiKey());
            updatedUser.addProperty("firstName", user.getFirstName());
            updatedUser.addProperty("phoneNumber", user.getPhoneNumber());

            return ResponseUtil.ok(gson.toJson(updatedUser));
        } else {
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Phone/authentication code pair is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(res));
        }
    }

    /**
     * POST /reset-password
     *
     * Reset password for a user, two methods one by providing current and new password for a non restricted
     * account, the other is to provide only the new password for a restricted account.
     * @param params
     * @return status of password reset
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordParams params) {
        User user = userService.getUserByApiKey(params.getApiKey());

        if (isValidCase(user, params)) {
            user.setPassword(PasswordUtil.encryptString(params.getNewPassword(), user.getSalt()));
            user.setRestricted(false);
            userService.updateUser(user);

            return ResponseUtil.ok(new ResponseMessage("ok", "Successfully updated password"));
        }

        return ResponseUtil.badRequest(new ResponseMessage(
                "error", "Bad parameters"));
    }

    /**
     * Checks if conditions are satisfied for updating password with just provided data
     * @param user
     * @param params
     * @return if this is a valid case to change password
     */
    private boolean isValidCase(User user, ResetPasswordParams params) {

        /* If the user is not restricted and a current password is provided */
        if (!user.isRestricted() && params.getCurrentPassword() != null) {

            /* Authenticate user and return result */
            return PasswordUtil.matchPassword(params.getCurrentPassword(), user.getSalt(), user.getPassword());
        }
        /* If the user is restricted and no current password has been provided */
        else if (user.isRestricted() && params.getCurrentPassword() == null) {
            return true;
        }

        return false;
    }
}
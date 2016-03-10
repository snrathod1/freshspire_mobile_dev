package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.params.NewUserParams;
import com.freshspire.api.model.params.PhoneNumberVerificationParams;
import com.freshspire.api.model.params.ResetPasswordParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by reesjones on 2/13/16.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /users/debug
     */
    @RequestMapping(value = "/debug", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getUsers() {
        ResponseMessage res = new ResponseMessage("ok", "Users endpoint is up. userService is: " + userService);
        return ResponseUtil.ok(res);
    }

    /**
     * GET /users/create
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getNewUserVerificationCode(@RequestParam String phoneNumber) {

        // If phone is null
        if(phoneNumber == null) {
            ResponseMessage error = new ResponseMessage("error", "Phone number is invalid");
            return ResponseUtil.badRequest(error);
        }

        Verification verification = AuthyClient.startAuthentication(phoneNumber);
        if(!verification.isOk()) {
            ResponseMessage error = new ResponseMessage("error",
                    "Could not send verification code. Is phone number formatted correctly?");
            return ResponseUtil.badRequest(error);
        }

        ResponseMessage res = new ResponseMessage("ok", "Message sent to " + phoneNumber);
        return ResponseUtil.ok(res);
    }

    /**
     * POST /users/create
     *
     * This creates a user. There are two requests required; the first has a phone number and password,
     * and sending that request sends a code to the phone number provided. The second request includes the phone
     * number, password, AND the confirmation code. Once this second request is received, the user is created and the
     * user object is returned.
     *
     * @return
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> createUser(@RequestBody NewUserParams params) {

        String code = params.getConfirmationCode();

        if(code == null) {
            ResponseMessage res = new ResponseMessage("error", "Please include confirmation code");
            return ResponseUtil.badRequest(res);
        }

        Verification verification = AuthyClient.checkAuthentication(params.getPhoneNumber(), code);

        if(!verification.isOk()) {
            ResponseMessage res = new ResponseMessage("error", "Invalid confirmation code");
            return ResponseUtil.badRequest(res);
        }

        // Verification was good, so now validate new user params and then create the user
        if(params.getPassword() == null
                || params.getFirstName() == null
                || params.getPhoneNumber() == null) {
            ResponseMessage res = new ResponseMessage("error", "Invalid request parameters");
            return ResponseUtil.badRequest(res);
        }

        // New user params were valid, so now create user object, insert into DB, and return it.
        String password = params.getPassword();

        // Make a new user object and populate its fields
        User newUser = new User();

        // Set fields specified in request parameters
        newUser.setFirstName(params.getFirstName());
        newUser.setPhoneNumber(params.getPhoneNumber());

        // Generate fields not in request parameters (API key, current time, etc.)
        newUser.setApiKey(PasswordUtil.generateApiKey());
        newUser.setCreated(new Date());
        newUser.setSalt(PasswordUtil.generateSaltString());
        newUser.setPassword(PasswordUtil.encryptString(password, newUser.getSalt()));

        userService.addUser(newUser);

        return ResponseUtil.ok(newUser);
    }

    /**
     * GET /users/forgot-password/{phoneNumber}
     *
     * Sends a message to the phone number to start the password reset process
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/forgot-password/{phoneNumber}", method = RequestMethod.GET, produces = "application/json")
    public String sendCodeForPasswordReset(@PathVariable String phoneNumber) {
        if (userService.getUserByPhoneNumber(phoneNumber) != null) {
            return sendVerificationMessage(phoneNumber);
        } else {
            return ResponseUtil.getStatusResponseString("No account for that phone number", "error").toString();
        }
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
    public String verifyCodeForPasswordReset(@RequestBody PhoneNumberVerificationParams params) {
        // Check with Authy if the code was correct
        Verification verification = AuthyClient.checkAuthentication(
                params.getPhoneNumber(),
                params.getVerificationCode());

        // If it is...
        if (verification.isOk()) {
            // Set the user associated with the phone number to restricted: true
            User user = userService.getUserByPhoneNumber(params.getPhoneNumber());
            user.setRestricted(true);
            userService.updateUser(user);

            return ResponseUtil.addToJson(
                "user",
                ResponseUtil.getStatusResponseString("user verified", "success"),
                gson.toJson(user)
            ).toString();
        } else {
            return ResponseUtil.getStatusResponseString(verification.getMessage(), verification.getSuccess()).toString();
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
    public String resetPassword(@RequestBody ResetPasswordParams params) {
        User user = userService.getUserByApiKey(params.getApiKey());

        if (isValidCase(user, params)) {
            user.setPassword(PasswordUtil.encryptString(params.getNewPassword(), user.getSalt()));
            user.setRestricted(false);
            userService.updateUser(user);

            return ResponseUtil.getStatusResponseString("updated password", "success").toString();
        }

        return ResponseUtil.getStatusResponseString("Please provide current password or use forgot password", "error").toString();
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

    private String sendVerificationMessage(String phoneNumber) {
        Verification verification = AuthyClient.startAuthentication(phoneNumber);
        String status = (verification.isOk() ? "ok" : "error");

        return ResponseUtil.getStatusResponseString(verification.getMessage(), status).toString();
    }
}
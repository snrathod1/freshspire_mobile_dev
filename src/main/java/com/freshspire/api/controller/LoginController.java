package com.freshspire.api.controller;

import com.freshspire.api.model.ApiKeyLoginParams;
import com.freshspire.api.model.LoginParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.freshspire.api.controller
 *
 * @created 2/29/16.
 */

@RestController
@RequestMapping("/users/login")
public class LoginController {

    @Autowired
    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * POST /users/login
     *
     * This logs in the user by accepting a phone number and password.
     *
     * @param params Phone number and password as JSON parameters in request body
     * @return The user object (if phone/pass were correct)
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String loginWithApiKey(@RequestBody LoginParams params) {
        User user = userService.getUserByPhoneNumber(params.getPhoneNumber());
        if(user == null) {
            return "{\"debug\": \"Invalid parameter! This should be returned with an error code\"}";
        }

        // Check that the password matches
        String inputPasswordHashed = PasswordUtil.encryptString(params.getPassword(), user.getSalt());

        if(inputPasswordHashed.equals(user.getPassword())) {
            return ResponseUtil.asJsonString(user, User.class);
        } else {
            return "{\"debug\": \"Password didn't match! This should be returned with an error code\"}";
        }

    }
}

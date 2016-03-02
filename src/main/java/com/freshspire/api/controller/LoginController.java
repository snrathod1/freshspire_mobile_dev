package com.freshspire.api.controller;

import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.params.LoginParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loginWithPhoneAndPassword(@RequestBody LoginParams params) {
        // Try to get the user by their phone number
        User user = userService.getUserByPhoneNumber(params.getPhoneNumber());

        // If there's no user for that phone number...
        if(user == null) {
            ResponseMessage res = new ResponseMessage("error", "Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(res, ResponseMessage.class));
        }

        // Check that the password matches
        String inputPasswordHashed = PasswordUtil.encryptString(params.getPassword(), user.getSalt());

        // If it does...
        if(inputPasswordHashed.equals(user.getPassword())) {

            // Return the user
            return ResponseUtil.ok(user);
        } else {

            // Password invalid, return 400 Bad Request
            ResponseMessage res = new ResponseMessage("error", "Bad request");
            return ResponseUtil.badRequest(res);
        }

    }
}

package com.freshspire.api.controller;

import com.freshspire.api.model.params.ApiKeyParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.freshspire.api.controller
 *
 * @created 2/29/16.
 */

@RestController
@RequestMapping("/users/key-login")
public class KeyLoginController {

    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(KeyLoginController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> loginWithApiKey(@RequestHeader("Authorization") String apiKey) {
        // If API key param is empty, return error
        if(apiKey.length() == 0)
            return ResponseUtil.badRequest("API key cannot be empty");

        // Try to find the user based on API key parameter
        User user = userService.getUserByApiKey(apiKey);

        // If user doesn't exist for that API key...
        if(user == null) {
            return ResponseUtil.unauthorized("Invalid API key");
        } else {
            // User was found, return it
            return ResponseUtil.makeUserObjectResponse(user, HttpStatus.OK);
        }
    }
}

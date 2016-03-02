package com.freshspire.api.controller;

import com.freshspire.api.model.params.ApiKeyLoginParams;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
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
@RequestMapping("/users/key-login")
public class KeyLoginController {

    @Autowired
    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(KeyLoginController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> loginWithApiKey(@RequestBody ApiKeyLoginParams params) {
        // Try to find the user based on API key parameter
        User user = userService.getUserByApiKey(params.getApiKey());

        // If user doesn't exist for that API key...
        if(user == null) {
            ResponseMessage res = new ResponseMessage("error", "Invalid API key");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(res, ResponseMessage.class));
        } else {
            // user was found, return it
            return ResponseEntity.ok(ResponseUtil.asJsonString(user, User.class));
        }
    }
}

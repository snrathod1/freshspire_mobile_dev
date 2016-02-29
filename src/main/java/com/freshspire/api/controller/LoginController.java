package com.freshspire.api.controller;

import com.freshspire.api.model.ApiKeyLoginParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
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

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String loginWithApiKey(@RequestBody ApiKeyLoginParams params) {
        User user = userService.getUserByApiKey(params.getApiKey());
        if(user == null) {
            return "{\"debug\": \"Invalid parameter! This should be returned with an error code\"}";
        } else {
            return ResponseUtil.asJsonString(user, User.class);
        }
    }
}

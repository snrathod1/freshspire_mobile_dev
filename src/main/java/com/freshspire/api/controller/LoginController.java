package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/users/login")
public class LoginController {

    @Autowired
    private UserService userService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "You're at the /users/login controller";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String createUserFromData(@RequestBody User user) {
        String password = user.getPassword();
        user.setApiKey(PasswordUtil.generateApiKey());
        user.setCreatedOn(new Date());
        user.setSalt(PasswordUtil.generateSaltString());
        user.setPassword(PasswordUtil.encryptString(password, user.getSalt()));
        userService.addUser(user);

        user.setPassword(password);
        user.setSalt(null);
        JSONObject statusResponse =  ResponseUtil.getStatusResponseString("Added User", true /* successfully added user */);
        String data = ResponseUtil.getDataJson(user, User.class);

        return ResponseUtil.addToJson("data", statusResponse, data).toString();
    }

    @RequestMapping(value = "/auth/{userId}/{password}", method = RequestMethod.GET, produces = "application/json")
    public String authUser(@PathVariable String userId, @PathVariable String password) {
        User user = getUserObjectById(userId);
        return ResponseUtil.getStatusResponseString(
                "Authenticating User",
                PasswordUtil.matchPassword(password, user.getSalt(), user.getPassword())
        ).toString();
    }

    @RequestMapping(value = "/create/{phoneNumber}", method = RequestMethod.GET, produces = "application/json")
    public String createUserWithPhone(@PathVariable String phoneNumber) {
        Verification verification = AuthyClient.startAuthentication(phoneNumber);
        return ResponseUtil.getStatusResponseString(verification.getMessage(), verification.isOk()).toString();
    }

    @RequestMapping(value = "/verify/{phoneNumber}/{authCode}", method = RequestMethod.GET, produces = "application/json")
    public String verifyNumber(@PathVariable String phoneNumber, @PathVariable String authCode) {
        Verification verification = AuthyClient.checkAuthentication(phoneNumber, authCode);
        return ResponseUtil.getStatusResponseString(verification.getMessage(), verification.isOk()).toString();
    }

    @RequestMapping(value = "/findUser/{userId}", method = RequestMethod.GET, produces = "application/json")
    public String getUserById(@PathVariable String userId) {
        User user = getUserObjectById(userId);
        user.setSalt(null);
        user.setPassword(null);
        return gson.toJson(getUserObjectById(userId));
    }

    @RequestMapping(value = "/fbauth/authId={authId}", method = RequestMethod.GET, produces = "application/json")
    public String hello(@PathVariable String authId)
            throws Exception {
        HttpResponse response = Unirest.get("https://graph.facebook.com/me")
                .queryString("access_token", authId)
                .asString();
        JSONObject json = new JSONObject(response);
        JSONObject body = new JSONObject(json.get("body"));
        String id = (String) body.get("id");
        return id;
    }

    private User getUserObjectById(String userId) {
        return userService.getUserById(userId);
    }
}

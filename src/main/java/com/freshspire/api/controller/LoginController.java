package com.freshspire.api.controller;

import com.authy.api.Params;
import com.authy.api.PhoneVerification;
import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/users/login")
public class LoginController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "You're at the /users/login controller";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String testing() {
        User user = new User();
        user.setAdmin(true);
        user.setApiKey("1234");
        user.setBanned(false);
        user.setCreatedOn(new Date());
        user.setFirstName("Aaditya Sriram");
        user.setPassword("testing");
        user.setPhoneNumber("+19196413035");
        user.setSalt("seasalt");
        user.setUserId("aadisriram");
        userService.addUser(user);
        return "Added new user";
    }

    @RequestMapping(value = "/create/{phoneNumber}", method = RequestMethod.GET, produces = "application/json")
    public String createUserWithPhone(@PathVariable String phoneNumber) {
        PhoneVerification phoneVerification = AuthyClient.getVerificationClient();

        Params params = new Params();
        params.setAttribute("locale", "en");

        Verification verification = phoneVerification.start(phoneNumber, "1", "sms", params);

        logger.info(verification.getMessage());
        logger.info(verification.getIsPorted());
        logger.info(verification.getSuccess());
        logger.info(String.valueOf(verification.isOk()));

        return new Gson().toJson("{ status:  { message: 'SMS sent', success: true}}");
    }

    @RequestMapping(value = "/verify/{phoneNumber}/{authCode}", method = RequestMethod.GET, produces = "application/json")
    public String verifyNumber(@PathVariable String phoneNumber, @PathVariable String authCode) {
        PhoneVerification phoneVerification = AuthyClient.getVerificationClient();

        Verification verification = phoneVerification.check(phoneNumber, "1", authCode);

        if (verification.isOk()) {
            return new Gson().toJson("{ status: { message: 'verified', success: true}}");
        }

        return new Gson().toJson("{ status: { message: 'wrong auth code', success: false}}");
    }


    @RequestMapping(value = "/findUser/{userId}", method = RequestMethod.GET, produces = "application/json")
    public String getUserById(@PathVariable String userId) {
        Gson gson = new Gson();
        String json = gson.toJson(userService.getUserById(userId));
        return json;
    }
}

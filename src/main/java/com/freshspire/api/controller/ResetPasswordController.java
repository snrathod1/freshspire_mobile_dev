package com.freshspire.api.controller;

import com.freshspire.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by reesjones on 2/13/16.
 */
@RestController
@RequestMapping("/users/reset-password")
public class ResetPasswordController {

    @RequestMapping(method = RequestMethod.GET)
    public String getResetPassword() {
        return "You're at the /users/reset-password controller";
    }
}

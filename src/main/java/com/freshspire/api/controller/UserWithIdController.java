package com.freshspire.api.controller;

import com.freshspire.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by reesjones on 2/13/16.
 */
@RestController
@RequestMapping("/users/{user_id}")
public class UserWithIdController {

    // TODO Better way to map this?
    // TODO this endpoint is not recognized (for example if you GET /freshspire/users/foobar, response is 404)
    @RequestMapping(value = "/users/{user_id}", method=RequestMethod.GET)
    public String getUserWithId(@PathVariable String userId){
        return String.format("You're at the /users/%s controller", userId);
    }
}

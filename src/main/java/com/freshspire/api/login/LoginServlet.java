package com.freshspire.api.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;

/**
 * Created by aadisriram on 2/7/16.
 */
@RestController
@RequestMapping("/login")
public class LoginServlet extends HttpServlet {

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        String result= "This works! Welcome to our RESTful service";
        return result;
    }
}

package com.freshspire.api.login;

import com.freshspire.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;

@RestController
@RequestMapping("/login")
public class LoginServlet extends HttpServlet {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "Working on this";
    }
}

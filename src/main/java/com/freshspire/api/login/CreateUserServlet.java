package com.freshspire.api.login;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aadisriram on 2/7/16.
 */
@RestController
@RequestMapping("/createUser")
public class CreateUserServlet {

    @RequestMapping(value = "/login={login}&authId={authId}", method = RequestMethod.GET, produces = "application/json")
    public String hello(@PathVariable String login, /* login type (Facebook, Google etc. */
                        @PathVariable String authId)
            throws Exception {
        HttpResponse response = Unirest.get("https://graph.facebook.com/me")
                            .queryString("access_token", authId)
                            .asString();

        return response.getBody().toString();
    }
}

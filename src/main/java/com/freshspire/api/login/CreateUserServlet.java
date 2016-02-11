package com.freshspire.api.login;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by aadisriram on 2/7/16.
 */
@RestController
@RequestMapping("/createuser")
public class CreateUserServlet {

    @RequestMapping(value = "/{authId}", method = RequestMethod.GET, produces = "application/json")
    public String hello(@PathVariable String authId) throws Exception {
        URL fbAuth = new URL("https://graph.facebook.com/me?access_token=" + authId);
        URLConnection urlConnection = fbAuth.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlConnection.getInputStream()));
        StringBuilder inputLineBuffer = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            inputLineBuffer.append(inputLine);
        }

        in.close();

        return inputLineBuffer.toString();
    }
}

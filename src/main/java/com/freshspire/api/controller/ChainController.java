package com.freshspire.api.controller;

import com.freshspire.api.model.Chain;
import com.freshspire.api.service.ChainService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chains")
public class ChainController {

    private ChainService chainService;
    private UserService userService;

    private static Gson gson = new Gson();

    @Autowired
    public void setChainService(ChainService chainService) {
        this.chainService = chainService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{chainId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getChainById(@PathVariable int chainId, @RequestHeader("Authorization") String apiKey) {

        // Authenticate user first
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        Chain chain = chainService.getChainById(chainId);

        if(chain == null) {
            return ResponseUtil.notFound("Chain with ID " + chain + " not found");
        }

        return ResponseEntity.ok(gson.toJson(chain));
    }
}

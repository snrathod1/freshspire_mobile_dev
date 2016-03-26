package com.freshspire.api.controller;

import com.freshspire.api.service.StoreService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/")
public class StoreController {

    private StoreService storeService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getStoreById(@PathVariable String storeId) {
        return ResponseUtil.ok(gson.toJson(storeService.getStore(storeId)));
    }

    @RequestMapping(value = "/discounts/{storeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountsInStoreById(@PathVariable String storeId) {
        return ResponseEntity.ok(gson.toJson(storeService.getDiscounts(storeId)));
    }
}

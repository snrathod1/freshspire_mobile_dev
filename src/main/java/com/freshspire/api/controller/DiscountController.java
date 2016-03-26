package com.freshspire.api.controller;

import com.freshspire.api.service.DiscountService;
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
@RequestMapping("/discounts")
public class DiscountController {

    private DiscountService discountService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(DiscountController.class);

    @Autowired
    public void setStoreService(DiscountService discountService) {
        this.discountService = discountService;
    }

    @RequestMapping(value = "/{discountId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountById(@PathVariable String discountId) {
        return ResponseEntity.ok(gson.toJson(discountService.getDiscountById(discountId)));
    }
}

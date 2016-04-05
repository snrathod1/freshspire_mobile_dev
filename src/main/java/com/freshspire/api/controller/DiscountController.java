package com.freshspire.api.controller;

import com.freshspire.api.service.DiscountService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @param discountId unique id of discount to fetch
     * @return unique discount data
     */
    @RequestMapping(value = "/{discountId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountById(@PathVariable String discountId) {
        return ResponseEntity.ok(gson.toJson(discountService.getDiscountById(discountId)));
    }

    /**
     * TODO: Returns only discounts for now, need to add location factor
     * @param latitude
     * @param longitude
     * @param apiKey
     * @param q
     * @param within
     * @param foodType
     * @param chain
     * @return list of discounts
     */
    @RequestMapping(value = "/{latitude}/{longitude}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountByLatLong(
            @PathVariable float latitude,
            @PathVariable float longitude,
            @RequestParam String apiKey,
            @RequestParam String q,
            @RequestParam float within,
            @RequestParam String foodType,
            @RequestParam String chain) {
        return ResponseEntity.ok(gson.toJson(discountService.getDiscountsByLatLong(latitude, longitude, q, within, foodType, chain)));
    }
}

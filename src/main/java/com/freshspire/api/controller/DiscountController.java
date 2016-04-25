package com.freshspire.api.controller;

import com.freshspire.api.model.CoordinatePair;
import com.freshspire.api.model.Discount;
import com.freshspire.api.service.DiscountService;
import com.freshspire.api.utils.AddressConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    /** The default value for the within parameter in discount requests */
    private static final Float DEFAULT_WITHIN = 10f;

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
    public ResponseEntity<String> getDiscountById(@PathVariable int discountId) {
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
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountsByLatLong(
            @RequestParam float latitude,
            @RequestParam float longitude,
            @RequestParam String apiKey,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float within,
            @RequestParam(required = false) String foodType,
            @RequestParam(required = false) String chain) {

        if(within == null) within = DEFAULT_WITHIN;
        List<Discount> discountList = discountService.getDiscountsByLatLong(latitude, longitude, q, within, foodType, chain);
        JsonObject body = new JsonObject();
        body.addProperty("count", discountList.size());
        body.add("discounts", gson.toJsonTree(discountList));

        return ResponseEntity.ok(gson.toJson(body));
    }

    @RequestMapping(value = "/place/{address}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountsByAddress(
            @PathVariable String address,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float within,
            @RequestParam(required = false) String foodType,
            @RequestParam(required = false) String chain,
            @RequestHeader("Authorization") String apiKey) {

        if(within == null) within = DEFAULT_WITHIN;
        CoordinatePair coordinates = AddressConverter.getLatLongFromAddress(address);

        return getDiscountsByLatLong(coordinates.getLatitude(), coordinates.getLongitude(), apiKey, q, within,
                foodType, chain);
    }
}

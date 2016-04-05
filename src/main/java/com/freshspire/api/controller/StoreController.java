package com.freshspire.api.controller;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.params.NewDiscountParams;
import com.freshspire.api.service.DiscountService;
import com.freshspire.api.service.StoreService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.omg.PortableInterceptor.DISCARDING;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stores/")
public class StoreController {

    private StoreService storeService;

    private UserService userService;

    private DiscountService discountService;

    private static Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * GET /stores/{storeId}
     *
     * TODO: ensure this complies with API specification
     *
     * @param storeId
     * @param apiKey
     * @return
     */
    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET)
    public ResponseEntity<String> getStoreById(@PathVariable String storeId, @RequestParam String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }
        Store store = storeService.getStoreById(storeId);
        if(store == null) {
            return ResponseUtil.notFound("Store not found!");
        }
        return ResponseUtil.makeStoreObjectResponse(store, HttpStatus.OK);
    }

    /**
     * GET /stores/{storeId}/discounts
     *
     * @param storeId
     * @param foodType
     * @param apiKey
     * @return
     */
    @RequestMapping(value = "/{storeId}/discounts", method = RequestMethod.GET)
    public ResponseEntity<String> getDiscountsForStore(@PathVariable String storeId,
                                                       @RequestParam(value = "foodType", required = false) String foodType,
                                                       @RequestParam String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        if(foodType == null) {
            List discounts = new ArrayList(5);
            discounts.add(new Discount(1, 1, new Date(0), new Date(10000)));
            discounts.add(new Discount(1, 2, new Date(500), new Date(12000)));
            discounts.add(new Discount(2, 3, new Date(1000), new Date(14000)));
            discounts.add(new Discount(1, 4, new Date(1500), new Date(16000)));
            discounts.add(new Discount(2, 5, new Date(2000), new Date(18000)));

        }
        return ResponseUtil.ok("Food types: " + foodType);
    }

    /**
     * POST /stores/{storeId}/discounts
     *
     * @param storeId
     * @param params
     * @param apiKey
     * @return
     */
    @RequestMapping(value = "/{storeId}/discounts", method = RequestMethod.POST)
    public ResponseEntity<String> addNewDiscountToStore(@PathVariable String storeId, @RequestBody NewDiscountParams params, @RequestParam String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        return ResponseUtil.ok("This will add a discount to the store");
    }
}

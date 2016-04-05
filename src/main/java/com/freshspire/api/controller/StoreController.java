package com.freshspire.api.controller;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.params.NewDiscountParams;
import com.freshspire.api.service.DiscountService;
import com.freshspire.api.service.StoreService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * GET /stores
     *
     * @return list of stores with discounts
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getStores(@RequestParam String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        return ResponseUtil.ok(gson.toJson(storeService.getStores()));
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
        // Authenticate user first
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        // Try to get Integer store ID from the storeId parameter
        int integerStoreId = -1;
        try {
            integerStoreId = Integer.parseInt(storeId);
        } catch(NumberFormatException e) {
            return ResponseUtil.badRequest("Bad request - are the parameters formatted correctly?");
        }

        Store store = storeService.getStoreById(integerStoreId);

        if(store == null) return ResponseUtil.notFound("Store with ID " + storeId + " not found");

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
    public ResponseEntity<String> getDiscountsForStore(
            @PathVariable String storeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String foodType,
            @RequestParam String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        List<Discount> discounts = storeService.getDiscounts(storeId, query, foodType);
        return ResponseUtil.ok(gson.toJson(discounts));
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

package com.freshspire.api.controller;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.params.NewDiscountParams;
import com.freshspire.api.service.DiscountService;
import com.freshspire.api.service.StoreService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
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
     * @return list of all stores with discounts
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getStores(@RequestHeader("Authorization") String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }
        List<Store> stores = storeService.getStores();
        JsonArray storesJson = new JsonArray();

        for(Store store : stores) {
            storesJson.add(gson.toJsonTree(store));
        }

        JsonObject body = new JsonObject();
        body.addProperty("count", stores.size());
        body.add("stores", storesJson);

        return ResponseEntity.status(HttpStatus.OK).body(body.toString());
    }

    /**
     * GET /stores/{storeId}
     *
     * @param storeId
     * @param apiKey
     * @return
     */
    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getStoreById(@PathVariable int storeId, @RequestHeader("Authorization") String apiKey) {
        // Authenticate user first
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        Store store = storeService.getStoreById(storeId);

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
    @RequestMapping(value = "/{storeId}/discounts", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDiscountsForStore(
            @PathVariable int storeId,
            @RequestHeader("Authorization") String apiKey,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String foodType) {
        // Authenticate first
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        if(storeService.getStoreById(storeId) == null) {
            return ResponseUtil.notFound("Store with ID " + storeId + " not found");
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
    @RequestMapping(value = "/{storeId}/discounts", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addNewDiscountToStore(@PathVariable int storeId, @RequestBody NewDiscountParams params, @RequestHeader("Authorization") String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        return ResponseUtil.ok("This will add a discount to the store");
    }
}

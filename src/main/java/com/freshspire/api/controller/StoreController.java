package com.freshspire.api.controller;

import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.param.NewDiscountParams;
import com.freshspire.api.model.param.NewStoreParams;
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
     * @param storeId Unique ID of the store
     * @param foodType Food type(s) to include in response. More than one food type is comma-separated.
     * @param apiKey The API key of a user
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
        System.out.println("discounts.size()=" + discounts.size());
        //for(Discount d : discounts) System.out.println(d);
        return ResponseUtil.ok(gson.toJson(discounts));
    }

    /**
     * POST /stores/{storeId}/discounts
     *
     * @param storeId
     * @param params
     * @param apiKey
     * @return store data
     */
    @RequestMapping(value = "/{storeId}/discounts", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addNewDiscountToStore(@PathVariable int storeId, @RequestBody NewDiscountParams params, @RequestHeader("Authorization") String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        long currentTime = System.currentTimeMillis()/1000;
        Discount newDiscount = new Discount(params.getStoreId(), params.getProductId(), currentTime,
                params.getExpirationDate(), params.getOriginalPrice(), params.getDiscountedPrice());

        discountService.addDiscount(newDiscount);

        return ResponseUtil.ok("Discount has been added to the store");
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Store>> getStoresByLocation(@RequestParam(required = false) float latitude, @RequestParam(required = false) float longitude) {
        return ResponseEntity.ok(storeService.getStoresByLatLong(latitude, longitude));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addStore(@RequestBody NewStoreParams params) {
        Store newStore = new Store(params.getChainId(), params.getDisplayName(), params.getStreet(), params.getCity(), params.getState(),
                params.getZipCode(), params.getLatitude(), params.getLongitude());

        storeService.addStore(newStore);

        return ResponseUtil.makeStoreObjectResponse(newStore, HttpStatus.CREATED);
    }
}

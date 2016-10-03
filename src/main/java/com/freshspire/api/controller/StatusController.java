package com.freshspire.api.controller;


import com.freshspire.api.service.DiscountService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    DiscountService discountService;

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
    public ResponseEntity<String> getStatus() {
        JsonObject body = new JsonObject();
        body.addProperty("status", "200");

        return ResponseEntity.status(HttpStatus.OK).body(body.toString());
    }

    @RequestMapping(value = "/database", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getDatabaseStatus() {
        JsonObject body = new JsonObject();
        try {
            if (discountService.isUp()) {
                body.addProperty("status", "200");
                return ResponseEntity.ok(body.toString());
            } else {
                body.addProperty("status", "503");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body.toString());
            }
        } catch(Exception e) {
            // TODO This is not a good solution to check that the database connection is up.
            body.addProperty("status", "503");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body.toString());
        }
    }
}

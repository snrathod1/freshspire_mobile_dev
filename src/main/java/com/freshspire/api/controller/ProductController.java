package com.freshspire.api.controller;

import com.freshspire.api.model.params.NewProductParams;
import com.freshspire.api.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> debug() {
        return ResponseUtil.ok("This is the products controller");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> createProduct(@RequestBody NewProductParams params) {
        return ResponseUtil.ok("Created product " + params.getDisplayName()); // TODO implement
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getProductById(@PathVariable int productId) {
        return ResponseUtil.ok("Found product " + productId);
    }

}

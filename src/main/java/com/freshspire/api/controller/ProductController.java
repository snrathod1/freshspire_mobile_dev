package com.freshspire.api.controller;

import com.freshspire.api.model.Product;
import com.freshspire.api.model.param.NewProductParams;
import com.freshspire.api.service.ProductService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private UserService userService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> debug() {
        return ResponseUtil.ok("This is the products controller");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> createProduct(@RequestParam("product") String productJson, 
                                                @RequestParam("thumbnail") MultipartFile thumbnailData,
                                                @RequestHeader("Authorization") String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }

        NewProductParams params = null;
        try {
            params = new ObjectMapper().readValue(productJson, NewProductParams.class);
        } catch (IOException e) {
            ResponseUtil.badRequest("Invalid product json");
        }

        String thumbnailUrl = null;
        try {
            thumbnailUrl = productService.saveThumbnail(thumbnailData);
        } catch (IOException e) {
            ResponseUtil.serverError("Unable to store thumbnail on the server");
        }

        Product newProduct = new Product(params.getDisplayName(), params.getChainId(), params.getFoodType(), thumbnailUrl);

        if(!productService.isValidFoodType(params.getFoodType())) {
            return ResponseUtil.badRequest("Invalid food type: " + params.getFoodType());
        }

        productService.addProduct(newProduct);

        return ResponseUtil.ok("Created product " + params.getDisplayName());
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getProductById(@PathVariable int productId, @RequestHeader("Authorization") String apiKey) {
        if(userService.getUserByApiKey(apiKey) == null) {
            return ResponseUtil.unauthorized("Unauthenticated");
        }
        Product product = productService.getProductById(productId);

        if(product == null) {
            return ResponseUtil.notFound("Product with ID " + productId + " not found");
        } else {
            return ResponseUtil.makeProductObjectResponse(product, HttpStatus.OK);
        }
    }

}

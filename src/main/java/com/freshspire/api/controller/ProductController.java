package com.freshspire.api.controller;

import com.freshspire.api.model.Product;
import com.freshspire.api.model.params.NewProductParams;
import com.freshspire.api.service.ProductService;
import com.freshspire.api.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> debug() {
        return ResponseUtil.ok("This is the products controller");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> createProduct(@RequestBody NewProductParams params) {
        Product newProduct = new Product(params.getDisplayName(), params.getChainId(), params.getFoodType());

        if(!productService.isValidFoodType(params.getFoodType())) {
            return ResponseUtil.badRequest("Invalid food type: " + params.getFoodType());
        }

        return ResponseUtil.ok("Created product " + params.getDisplayName());
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getProductById(@PathVariable int productId) {
        Product product = productService.getProductById(productId);

        if(product == null) {
            return ResponseUtil.notFound("Product with ID " + productId + " not found");
        } else {
            return ResponseUtil.makeProductObjectResponse(product, HttpStatus.OK);
        }
    }

}

package com.freshspire.api.service;

import com.freshspire.api.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(int productId);
    List<Product> getProductsByChain(int chainId);
    void addProduct(Product product);
    void deleteProduct(Product product);
    void updateProduct(Product product);
    boolean isValidFoodType(String foodType);
}

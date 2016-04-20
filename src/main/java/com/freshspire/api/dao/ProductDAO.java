package com.freshspire.api.dao;

import com.freshspire.api.model.Product;

import java.util.List;

public interface ProductDAO {

    void addProduct(Product product);
    void deleteProduct(Product product);
    void updateProduct(Product product);
    Product getProductById(int productId);
    List<Product> getProductsByChain(int chainId);
}

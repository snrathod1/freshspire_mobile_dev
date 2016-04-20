package com.freshspire.api.service;

import com.freshspire.api.dao.ProductDAO;
import com.freshspire.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Transactional
    @Override
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    @Transactional
    @Override
    public List<Product> getProductsByChain(int chainId) {
        return productDAO.getProductsByChain(chainId);
    }

    @Transactional
    @Override
    public void addProduct(Product product) {
        productDAO.addProduct(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Product product) {
        productDAO.deleteProduct(product);
    }

    @Transactional
    @Override
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    public boolean isValidFoodType(String foodType) {
        return foodType.equals("meat")
                || foodType.equals("produce")
                || foodType.equals("dairy")
                || foodType.equals("bakery");
    }
}

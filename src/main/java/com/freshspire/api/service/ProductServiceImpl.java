package com.freshspire.api.service;

import com.freshspire.api.dao.ProductDAO;
import com.freshspire.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String THUMBNAIL_BASE_URI = "src/main/webapp/WEB-INF/static/images/products";
    private static final String THUMBNAIL_EXTENSION = "jpg";

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

    @Override
    public synchronized String saveThumbnail(int chainId, MultipartFile thumbnailData) throws IOException {
        // Generate thumbnail file path
        String thumbnailFilePath = String.format("%s/%d/%d.%s", THUMBNAIL_BASE_URI, chainId,
                System.currentTimeMillis(), THUMBNAIL_EXTENSION);

        // Create stub thumbnail file on the server
        File thumbnailFile = new File(thumbnailFilePath);
        if (!thumbnailFile.exists() && !thumbnailFile.getParentFile().mkdirs() && !thumbnailFile.createNewFile()) {
            throw new IOException();
        }

        // Write thumbnail data to the stub file
        thumbnailData.transferTo(thumbnailFile);

        // Return the absolute location of the thumbnail image file
        return thumbnailFilePath;
    }
}

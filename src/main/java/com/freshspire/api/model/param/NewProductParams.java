package com.freshspire.api.model.param;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class NewProductParams {

    private String displayName;

    private String foodType;

    private int chainId;

    private CommonsMultipartFile thumbnail;

    public NewProductParams() {}

    public NewProductParams(String displayName, String foodType, int chainId, CommonsMultipartFile thumbnail) {
        this.displayName = displayName;
        this.foodType = foodType;
        this.chainId = chainId;
        this.thumbnail = thumbnail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public CommonsMultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(CommonsMultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }
}

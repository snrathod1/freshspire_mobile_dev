package com.freshspire.api.model.params;

public class NewProductParams {

    private String displayName;

    private String foodType;

    private int chainId;

    public NewProductParams() {}

    public NewProductParams(String displayName, String foodType, int chainId) {
        this.displayName = displayName;
        this.foodType = foodType;
        this.chainId = chainId;
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
}

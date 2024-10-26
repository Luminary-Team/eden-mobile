package com.eden.api.dto;

public class RegisterFavoriteRequest {
    private int userId;
    private int productId;

    public RegisterFavoriteRequest(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}

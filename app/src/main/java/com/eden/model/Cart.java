package com.eden.model;

public class Cart {
    private int cartId;
    private int productId;

    public Cart(int cartId, int productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public int getUserId() {
        return cartId;
    }

    public void setUserId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}

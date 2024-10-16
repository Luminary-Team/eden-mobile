package com.eden.model;

public class Cart {
    private int cartId;
    private int productsId;

    public Cart(int cartId, int productsId) {
        this.cartId = cartId;
        this.productsId = productsId;
    }

    public int getUserId() {
        return cartId;
    }

    public void setUserId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productsId;
    }

    public void setProductId(int productsId) {
        this.productsId = productsId;
    }
}

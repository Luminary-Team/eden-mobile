package com.eden.api.dto;

import com.eden.model.Product;

public class CartResponse {
    private int cartItemId;
    private int cartId;
    private Product product;

    public CartResponse(int cartItemId, int cartId, Product product) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.product = product;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

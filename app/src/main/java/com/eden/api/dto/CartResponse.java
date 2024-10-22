package com.eden.api.dto;

import com.eden.model.Product;

import java.util.List;

public class CartResponse {
    private List<CartItemResponse> cartItems;
    private float totalSale;

    public CartResponse(List<CartItemResponse> cartItems, float totalSale) {
        this.cartItems = cartItems;
        this.totalSale = totalSale;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    public float getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(float totalSale) {
        this.totalSale = totalSale;
    }
}

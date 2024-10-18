package com.eden.api.dto;

public class OrderRequest {
    private int cartId;
    private int paymentTypeId;
    private String addressDelivery;

    public OrderRequest(int cartId, int paymentTypeId, String addressDelivery) {
        this.cartId = cartId;
        this.paymentTypeId = paymentTypeId;
        this.addressDelivery = addressDelivery;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(String addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

}

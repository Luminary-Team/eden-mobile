package com.eden.api.dto;

import com.eden.model.PaymentType;
import com.eden.model.StatusOrder;

public class OrderResponse {

    private PaymentType paymentType;
    private StatusOrder statusOrder;
    private String orderDate;
    private String addressDelivery;
    private float totalValue;

    public OrderResponse(PaymentType paymentType, StatusOrder statusOrder, String orderDate, String addressDelivery, float totalValue) {
        this.paymentType = paymentType;
        this.statusOrder = statusOrder;
        this.orderDate = orderDate;
        this.addressDelivery = addressDelivery;
        this.totalValue = totalValue;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public StatusOrder getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(StatusOrder statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(String addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }
}

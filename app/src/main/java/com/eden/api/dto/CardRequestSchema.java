package com.eden.api.dto;

public class CardRequestSchema {
    private int userId;
    private String cardNumber;
    private String cvv;
    private String cvc;
    private String validity;

    public CardRequestSchema(int userId, String cardNumber, String cvv, String cvc, String validity) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.cvc = cvc;
        this.validity = validity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
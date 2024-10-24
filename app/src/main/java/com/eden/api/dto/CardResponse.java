package com.eden.api.dto;

public class CardResponse {

    private int id;
    private int userId;
    private String cardNumber;
    private String validity;

    public CardResponse(int id, int userId, String cardNumber, String validity) {
        this.id = id;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.validity = validity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
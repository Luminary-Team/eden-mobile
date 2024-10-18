package com.eden.model;

public class PaymentType {
    private int id;
    private String type;
    private String description;

    public PaymentType(int id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

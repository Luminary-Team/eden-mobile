package com.eden.model;

import java.math.BigInteger;

public class Product {
    private BigInteger usageTimeId;
    private BigInteger conditionTypeId;
    private String email;
    private String title;
    private String description;
    private String price;
    private String senderZipCode;
    private float rating;
    private int stock;

    public Product() { }

    public Product(BigInteger usageTimeId, BigInteger conditionTypeId, String email, String title, String description, String price, String senderZipCode, float rating, int stock) {
        this.usageTimeId = usageTimeId;
        this.conditionTypeId = conditionTypeId;
        this.email = email;
        this.title = title;
        this.description = description;
        this.price = price;
        this.senderZipCode = senderZipCode;
        this.rating = rating;
        this.stock = stock;
    }
}

package com.eden.model;

public class Product {
    private int id;
    private int usageTimeId;
    private int conditionTypeId;
    private int userId;
    private String title;
    private String description;
    private float price;
    private float maxPrice;
    private String senderZipCode;
    private float rating;

    public Product() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsageTimeId() {
        return usageTimeId;
    }

    public void setUsageTimeId(int usageTimeId) {
        this.usageTimeId = usageTimeId;
    }

    public int getConditionTypeId() {
        return conditionTypeId;
    }

    public void setConditionTypeId(int conditionTypeId) {
        this.conditionTypeId = conditionTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSenderZipCode() {
        return senderZipCode;
    }

    public void setSenderZipCode(String senderZipCode) {
        this.senderZipCode = senderZipCode;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Product(int id, int usageTimeId, int conditionTypeId, int userId, String title, String description, float price, float maxPrice, String senderZipCode, float rating) {
        this.id = id;
        this.usageTimeId = usageTimeId;
        this.conditionTypeId = conditionTypeId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.maxPrice = maxPrice;
        this.senderZipCode = senderZipCode;
        this.rating = rating;
    }
}

package com.eden.api.dto;

public class ProductRequest {

    private long usageTimeId;
    private long conditionTypeId;
    private String userEmail;
    private String title;
    private String description;
    private float price;
    private String senderZipCode;
    private boolean premium;

    public ProductRequest() {
    }

    public ProductRequest(long usageTimeId, long conditionTypeId, String userEmail, String title, String description, float price, String senderZipCode, boolean premium) {
        this.usageTimeId = usageTimeId;
        this.conditionTypeId = conditionTypeId;
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.price = price;
        this.senderZipCode = senderZipCode;
        this.premium = premium;
    }

    public long getUsageTimeId() {
        return usageTimeId;
    }

    public void setUsageTimeId(long usageTimeId) {
        this.usageTimeId = usageTimeId;
    }

    public long getConditionTypeId() {
        return conditionTypeId;
    }

    public void setConditionTypeId(long conditionTypeId) {
        this.conditionTypeId = conditionTypeId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getSenderZipCode() {
        return senderZipCode;
    }

    public void setSenderZipCode(String senderZipCode) {
        this.senderZipCode = senderZipCode;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}

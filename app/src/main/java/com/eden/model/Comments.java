package com.eden.model;

public class Comments {
    private long id;
    private long productId;
    private long userId;
    private String content;

    public Comments(long id, long productId, long userId, String content) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

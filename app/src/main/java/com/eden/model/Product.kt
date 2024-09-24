package com.eden.model;

public class Product {
    private long id;
    private long conditionTypeId;
    private long usersId;
    private String name;
    private float value;
    private String description;
    private String urlImage;
    private float avaliation;
    private int stock;

    public Product() {

    }

    public Product(long id, long conditionTypeId, long usersId, String name, float value, String description, String urlImage, float avaliation, int stock) {
        this.id = id;
        this.conditionTypeId = conditionTypeId;
        this.usersId = usersId;
        this.name = name;
        this.value = value;
        this.description = description;
        this.urlImage = urlImage;
        this.avaliation = avaliation;
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConditionTypeId() {
        return conditionTypeId;
    }

    public void setConditionTypeId(long conditionTypeId) {
        this.conditionTypeId = conditionTypeId;
    }

    public long getUsersId() {
        return usersId;
    }

    public void setUsersId(long usersId) {
        this.usersId = usersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public float getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(float avaliation) {
        this.avaliation = avaliation;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

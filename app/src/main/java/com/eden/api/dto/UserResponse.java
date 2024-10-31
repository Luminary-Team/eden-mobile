package com.eden.api.dto;

import com.eden.model.Product;
import java.util.List;

public class UserResponse {
    private String cpf;
    private String name;
    private String userName;
    private String password;
    private float rating;
    private String email;
    private String cellphone;
    private int cartId;
    private List<Product> products;

    public UserResponse(String cpf, String name, String userName, String password, float rating, String email, String cellphone, int cartId, List<Product> products) {
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.cellphone = cellphone;
        this.cartId = cartId;
        this.products = products;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
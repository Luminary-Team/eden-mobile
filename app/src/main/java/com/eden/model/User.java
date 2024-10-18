package com.eden.model;

public class User {
    public String cpf;
    public String name;
    public String userName;
    public String password;
    public float rating;
    public String email;
    public String cellphone;

    public User(String cpf, String name, String userName, String password, float rating, String email, String cellphone) {
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.cellphone = cellphone;
    }

    public User() {

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
}

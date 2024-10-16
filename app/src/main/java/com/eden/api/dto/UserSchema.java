package com.eden.api.dto;

public class UserSchema {
    private int id;
    private String cpf;
    private String name;
    private String userName;
    private String password;
    private float rating;
    private String email;
    private String cellphone;

    public UserSchema() {
    }

    public UserSchema(int id, String cpf, String name, String userName, String password, float rating, String email, String cellphone) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.cellphone = cellphone;
    }

    public void setUserSchema(String cpf, String name, String userName, String password, float rating, String email, String cellphone) {
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.cellphone = cellphone;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserSchema{" +
                "id='" + id + '\'' +
                ", cpf='" + cpf + '\'' +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", rating=" + rating +
                ", email='" + email + '\'' +
                ", cellphone='" + cellphone + '\'' +
                '}';
    }
}

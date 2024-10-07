package com.eden.api.dto;

import java.io.Serializable;

public class UserSchema implements Serializable {
    private String id;
    private String cpf;
    private String name;
    private String userName;
    private String password;
    private float rating;
    private String email;
    private String cellphone;

    public UserSchema() {
    }

    public UserSchema(String id, String cpf, String name, String userName, String password, float rating, String email, String cellphone) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.cellphone = cellphone;
    }

    public String getId() {
        return id;
    }
}

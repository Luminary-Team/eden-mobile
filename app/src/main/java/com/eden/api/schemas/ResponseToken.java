package com.eden.api.schemas;

public class ResponseToken {

    public String email;
    public String password;

    public ResponseToken(String email, String password) {
        this.email = email;
        this.password = password;
    }

}

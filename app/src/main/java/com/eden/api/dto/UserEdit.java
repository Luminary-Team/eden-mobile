package com.eden.api.dto;

public class UserEdit {
    private String name;
    private String userName;
    private String cellphone;
    private String cpf;

    public UserEdit() {}

    public UserEdit(String name, String userName, String cellphone, String cpf) {
        this.name = name;
        this.userName = userName;
        this.cellphone = cellphone;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}

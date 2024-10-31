package com.eden.api.dto;

import com.eden.model.Product;

import java.util.List;

public class OrderGetAllResponse {
    List<Product> productList;

    public OrderGetAllResponse(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

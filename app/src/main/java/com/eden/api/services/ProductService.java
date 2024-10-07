package com.eden.api.services;

import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getProducts();
}
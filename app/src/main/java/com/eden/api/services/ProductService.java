package com.eden.api.services;

import com.eden.api.dto.ProductRequest;
import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductService {
    @GET("/product/getAll")
    Call<List<Product>> getAllProducts();
    @POST("/product/register")
    Call<Product> registerProduct(@Body ProductRequest productRequest);
}

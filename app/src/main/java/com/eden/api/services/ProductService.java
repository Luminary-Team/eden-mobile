package com.eden.api.services;

import com.eden.api.dto.ProductRequest;
import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/product/findProducts")
    Call<List<Product>> getAllProducts();
    @GET("/product/getByTitle")
    Call<List<Product>> getProductByTitle(@Query("title") String title);
    @GET("/product/getByUserId/{id}")
    Call<List<Product>> getProductsByUserId(@Path("id") int id);
    @POST("/product/register")
    Call<Product> registerProduct(@Body ProductRequest productRequest);
}

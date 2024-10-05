package com.eden.api;

import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProdutoApi {
    @GET("products")
    Call<List<Product>> getProducts();
}

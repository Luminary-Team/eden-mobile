package com.eden.api.services;

import com.eden.api.dto.ProductRequest;
import com.eden.api.dto.UserEditRequest;
import com.eden.api.dto.UserSchema;
import com.eden.model.Product;
import com.eden.utils.AndroidUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/product/getProducts")
    Call<List<Product>> getAllProducts(@Header("userId") int userId);
    @GET("/product/getPremiumProducts")
    Call<List<Product>> getPremiumProducts(@Header("userId") int userId);
    @GET("/product/getByTitle")
    Call<List<Product>> getProductByTitle(@Query("title") String title);
    @GET("/product/getByUserId/{id}")
    Call<List<Product>> getProductsByUserId(@Path("id") int id);
    @POST("/product/register")
    Call<Product> registerProduct(@Body ProductRequest productRequest);
    @PATCH("/product/update/{id}")
    Call<Product> updateProduct(@Body ProductRequest productRequest, @Path("id") int id);
    @DELETE("/product/delete/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}

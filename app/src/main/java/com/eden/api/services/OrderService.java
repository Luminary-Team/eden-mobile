package com.eden.api.services;

import com.eden.api.dto.OrderGetAllResponse;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {

    @POST("/order/registerOrder")
    Call<OrderResponse> registerOrder(@Body OrderRequest orderRequest);

    @GET("/order/getAll/{userId}")
    Call<OrderGetAllResponse> getOrders(@Path("userId") String userId);

}

package com.eden.api.services;

import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderService {

    @POST("/order/registerOrder")
    Call<OrderResponse> registerOrder(@Body OrderRequest orderRequest);

}

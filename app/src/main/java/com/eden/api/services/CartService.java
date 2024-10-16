package com.eden.api.services;

import com.eden.api.dto.CartResponse;
import com.eden.model.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CartService {

    @POST("/cart/register")
        Call<CartResponse> registerCart(@Body Cart cart);

}

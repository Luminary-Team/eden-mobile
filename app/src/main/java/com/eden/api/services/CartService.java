package com.eden.api.services;

import com.eden.api.dto.CartResponse;
import com.eden.model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartService {

    @GET("/cart/getCartItemsByCartId/{cartId}")
        Call<List<CartResponse>> getCartItemsByCartId(@Path("cartId") int cartId);

    @POST("/cart/register")
        Call<CartResponse> registerCart(@Body Cart cart);

}

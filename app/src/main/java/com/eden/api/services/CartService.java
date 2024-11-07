package com.eden.api.services;

import com.eden.api.dto.CartItemResponse;
import com.eden.api.dto.CartResponse;
import com.eden.model.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {

    @GET("/cart/getCartItemsByCartId/{cartId}")
    Call<CartResponse> getCartItemsByCartId(@Path("cartId") int cartId);

    @POST("/cart/register")
    Call<CartItemResponse> registerCart(@Body Cart cart);

    @DELETE("/cart/deleteCartItem/{cartItemId}")
    Call<Void> deleteCartItem(@Path("cartItemId") int cartItemId, @Query("cartId") int cartId);

}

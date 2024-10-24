package com.eden.api.services;

import com.eden.api.dto.CardRequestSchema;
import com.eden.api.dto.CardResponse;
import com.eden.model.Card;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardService {

    @POST("/card/")
    Call<Card> createCard(@Body CardRequestSchema card);

    @GET("/card/user/{userId}")
    Call<List<CardResponse>> getCardByUserId(@Path("userId") int userId);

}

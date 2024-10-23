package com.eden.api.services;

import com.eden.api.dto.CardRequestSchema;
import com.eden.model.Card;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CardService {

    @POST("/card/")
    Call<Card> createCard(@Body CardRequestSchema card);

}

package com.eden.api;

import com.eden.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("/users")
    Call<List<User>> getUser();
}

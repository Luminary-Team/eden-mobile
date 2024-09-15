package com.eden.api;

import com.eden.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface UserApi {
    @GET("/users")
    Call<List<User>> getUsers();

    @POST("/register")
    Call<User> userRegister(String email, String password);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

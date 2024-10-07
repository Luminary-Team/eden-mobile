package com.eden.api.services;

import com.eden.api.dto.TokenRequest;
import com.eden.api.dto.UserSchema;
import com.eden.model.Token;
import com.eden.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserService {

    @GET("/user/findByParam")
    Call<UserSchema> findByParam(@Query("email") String email);

    @POST("/user/token")
    Call<Token> getToken(@Body TokenRequest requestToken);

    @POST("/user/register")
    Call<ResponseBody> userRegister(@Body User user);

    @PATCH("/user/update/{id}")
    Call<UserSchema> updateUser(@Body User user, @Path("id") String id);

}

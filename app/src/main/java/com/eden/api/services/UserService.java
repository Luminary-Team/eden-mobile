package com.eden.api.services;

import com.eden.api.dto.FavoriteRequest;
import com.eden.api.dto.TokenRequest;
import com.eden.api.dto.UserEditRequest;
import com.eden.api.dto.UserResponse;
import com.eden.api.dto.UserSchema;
import com.eden.model.Product;
import com.eden.model.Token;
import com.eden.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserService {

    @POST("/user/token")
    Call<Token> getToken(@Body TokenRequest requestToken);

    @POST("/user/register")
    Call<ResponseBody> userRegister(@Body User user);

    @GET("/user/getParam")
    Call<UserSchema> getParam(@Query("email") String email);

    @PATCH("/user/update/{id}")
    Call<UserSchema> updateUser(@Body UserEditRequest user, @Path("id") String id);

    @GET("/user/favorites/{userId}")
    Call<List<Product>> getFavorites(@Path("userId") String userId);

    @POST("/user/favorites")
    Call<UserResponse> registerFavorite(@Body FavoriteRequest favoriteRequest);

    @POST("/user/favorites")
    Call<UserResponse> deleteFavorite(@Body FavoriteRequest favoriteRequest);
}

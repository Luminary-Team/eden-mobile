package com.eden.api;

import com.eden.api.schemas.ResponseToken;
import com.eden.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserApi {

    @POST("/user/token")
    Call<?> getToken(@Body ResponseToken requestToken);

    @POST("/user/register")
    Call<ResponseBody> userRegister(@Body User user);

}

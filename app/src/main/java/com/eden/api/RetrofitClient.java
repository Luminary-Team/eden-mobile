package com.eden.api;

import static com.eden.utils.AndroidUtil.getToken;

import android.util.Log;

import com.eden.model.Token;
import com.eden.utils.AndroidUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static String baseUrl ="https://desenvolvimento-ii.onrender.com/";
    private static final String baseUrlMongo ="https://eden-api-mongo.onrender.com/";
    private static final String baseUrlAws = "http://ec2-107-20-132-70.compute-1.amazonaws.com:8080/";

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static Retrofit getClientMongo() {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlMongo)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static Retrofit getClientWithToken() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(AndroidUtil.token))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }

    public static void changeService() {
        baseUrl = baseUrlAws;
    }

}
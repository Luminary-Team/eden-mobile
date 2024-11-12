package com.eden.api;

import static com.eden.utils.AndroidUtil.getToken;

import android.util.Log;

import com.eden.model.Token;
import com.eden.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static String baseUrl = "http://107.20.132.70:8080/"; // SQL AWS - Pedro
    private static String baseUrlMongo = "http://3.94.25.250:8080/"; // MONGODB AWS - Pedro

    private static final List<String> baseUrls = List.of(
            "https://eden-api-mongo.onrender.com/", // MongoDB render - Luminary
            "https://desenvolvimento-ii.onrender.com/", // SQL render - Luminary
            "https://eden-api-mongo-a2dh.onrender.com/", // MongoDB render - Willamy
            "https://eden-api-w9pm.onrender.com/" // SQL render - Willamy
    );

    private static int currentUrlIndex = 0;

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("RetrofitClient", "baseUrl: " + baseUrl);

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

        Log.d("RetrofitClient", "baseUrl: " + baseUrl);

        return retrofit;

    }

    public static void changeService() {
        // Tenta mudar para a próxima URL
        if (currentUrlIndex < baseUrls.size() - 1) {
            currentUrlIndex++;
            baseUrl = baseUrls.get(currentUrlIndex);
            Log.d("RetrofitClient", "Mudando para a URL: " + baseUrl);
        } else {
            Log.d("RetrofitClient", "Todas as URLs foram tentadas.");
        }
    }

}
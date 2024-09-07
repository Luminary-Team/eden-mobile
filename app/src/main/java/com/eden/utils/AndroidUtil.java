package com.eden.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.UserRegister;
import com.eden.api.UserApi;
import com.eden.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AndroidUtil {

    public static void openActivity(Context context, Class<?> className) {
        context.startActivity(new Intent(context, className));
    }
    public static void setProductImage(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void getUsers(Context context) {

        Retrofit retrofit;

        // Configurar a API
        retrofit = new Retrofit.Builder()
                .baseUrl("apiDoPedro")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criar chamada
        UserApi userApi = retrofit.create(UserApi.class);
        Call<List<User>> call = userApi.getUsers();

        // Fazer chamada
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> userList = response.body();
                if (userList != null) {
                    Toast.makeText(context, userList.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                Toast.makeText(context, "Deu eles", Toast.LENGTH_SHORT).show();
            }

        });
    }

}

package com.eden.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.UserRegister;
import com.eden.api.UserApi;
import com.eden.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AndroidUtil {

    // Abrir intent
    public static void openActivity(Context context, Class<?> className) {
        context.startActivity(new Intent(context, className));
    }

    // IMAGES
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = storage.getReference();

    public static void setProductImage(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void uploadImageToFirebase(Uri uri, String imageName) {
        // Criando pasta referência "images" no firebase
        StorageReference imageRef = storageRef.child("images/" + imageName + ".jpg");

        // Subindo a imagem pro firebase
        UploadTask uploadTask = imageRef.putFile(uri);
        uploadTask.addOnSuccessListener(v -> {
            Log.d("CHECKPOINT", "Image uploaded successfully!");
            imageRef.getDownloadUrl().addOnSuccessListener(taskSnapshot -> {
                Log.d("CHECKPOINT", "Download URL: " + uri.toString());
            }).addOnFailureListener(e -> {
                Log.e("CHECKPOINT", "Error uploading image: " + e.getMessage());
            });
        });
    }

    public static void downloadImageFromFirebase(Context context, String imageName, ImageView imageView) {
        // Crie uma referência para a imagem
        StorageReference imageRef = storageRef.child("images/" + imageName + ".jpg");

        // Baixe a imagem do Firebase Storage
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("CHECKPOINT", "Download URL: " + uri.toString());
            // Use a URL de download para exibir a imagem
            Glide.with(context)
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Log.e("CHECKPOINT", "Erro ao baixar a imagem: " + e.getMessage());
        });
    }

    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private boolean allPermissionsGranted(Context context) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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

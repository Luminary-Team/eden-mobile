package com.eden.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.TokenRequest;
import com.eden.api.dto.UserSchema;
import com.eden.api.services.UserService;
import com.eden.model.Token;
import com.eden.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AndroidUtil {
    public static String token;
    public static UserSchema currentUser;

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

    public static void uploadImageToFirebase(Uri uri) {
        // Criando pasta referência "images" no firebase
        StorageReference imageRef = storageRef.child("images/ProfilePic_" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

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

    public static void downloadImageFromFirebase(Context context, ImageView imageView) {
        // Crie uma referência para a imagem
        StorageReference imageRef = storageRef.child("images/ProfilePic_" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + ".jpg");

        // Baixe a imagem do Firebase Storage
        imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    if (context instanceof Activity && !((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        Log.d("CHECKPOINT", "Download URL: " + uri.toString());
                        // Use a URL de download para exibir a imagem
                        Glide.with(context)
                                .load(uri)
                                .into(imageView);
                    } else {
                        Log.w("AndroidUtil", "Activity is no longer valid, cannot load image");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("CHECKPOINT", "Erro ao baixar a imagem: " + e.getMessage());
                });
    }

    public static void getToken() {
        Retrofit client = RetrofitClient.getClient();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        UserService service = client.create(UserService.class);
        Call<Token> call = service.getToken(new TokenRequest(email));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    token = response.body().getToken();
                    getUser();
                    Log.d("TOKEN", "Token: " + token);
                } else {
                    Log.d("TOKEN", "Erro ao obter o token");
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Log.d("TOKEN", throwable.getMessage());
            }
        });
    }

    public static CompletableFuture<UserSchema> getUser() {
        UserService service = RetrofitClient.getClientWithToken().create(UserService.class);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Call<UserSchema> call = service.getParam(email);
        CompletableFuture<UserSchema> future = new CompletableFuture<>();

        call.enqueue(new Callback<UserSchema>() {
            @Override
            public void onResponse(Call<UserSchema> call, Response<UserSchema> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    UserSchema user = response.body();
                    future.complete(user);
                    Log.d("USER", "User: " + currentUser.toString());
                } else {
                    try {
                        Log.d("ErrorBody", response.errorBody().string());
                        future.completeExceptionally(new Throwable("Failed to get user"));
                    } catch (IOException e) {
                        future.completeExceptionally(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSchema> call, Throwable throwable) {
                Log.d("CHECKPOINT", throwable.getMessage());
                future.completeExceptionally(throwable);
            }
        });
        return future;
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

//    public static void notificar(Context context, Context localContext) {
//
//        // Criar Notificação
//        Intent intent = new Intent(context, NotificaionReceiver.class);
//        PendingIntent pendindIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(localContext, "channel_id")
//                .setSmallIcon(R.drawable.eden_logotipo_2)
//                .setContentTitle("Título da notificação")
//                .setContentText("CLIQUE E RECEBA!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .setContentIntent(pendindIntent);
//
//        // Criar Canal de Notificação
//        NotificationChannel channel = new NotificationChannel("channel_id", "Notificar",
//                NotificationManager.IMPORTANCE_HIGH);
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(channel);
//
//        // Mostrar notificação
//        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(localContext);
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        notificationCompat.notify(1, builder.build());
//
//
//    }

}

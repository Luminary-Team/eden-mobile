package com.eden.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.OrderGetAllResponse;
import com.eden.api.dto.TokenRequest;
import com.eden.api.dto.UserSchema;
import com.eden.api.services.OrderService;
import com.eden.api.services.UserService;
import com.eden.model.Product;
import com.eden.model.Token;
import com.eden.ui.activities.MainActivity;
import com.eden.callbacks.UserCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AndroidUtil {
    public static String token;
    public static UserSchema currentUser;
    public static List<Product> favorites, boughtProducts;

    // Open intent
    public static void openActivity(Context context, Class<?> className) {
        context.startActivity(new Intent(context, className));
    }

    // IMAGES
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageRef = storage.getReference();

    public static void setProductImage(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void fetchFavorites() {
        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);
        Call<List<Product>> call = userService.getFavorites(String.valueOf(currentUser.getId()));
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> favs = response.body();
                    favorites = favs;
                } else {
                    try {
                        Log.d("FAVORITES", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Log.e("FAVORITES", throwable.getMessage());
            }
        });
    }

    public static void fetchBoughtProducts() {
        OrderService orderService = RetrofitClient.getClient().create(OrderService.class);
        Call<OrderGetAllResponse> call = orderService.getOrders(String.valueOf(currentUser.getId()));
        call.enqueue(new Callback<OrderGetAllResponse>() {
            @Override
            public void onResponse(Call<OrderGetAllResponse> call, Response<OrderGetAllResponse> response) {
                if (response.isSuccessful()) {
                    OrderGetAllResponse jsonOrder = response.body();
                    List<Product> orders = jsonOrder.getProductList();

                    boughtProducts = orders;

                } else {
                    try {
                        Log.d("ORDERS", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderGetAllResponse> call, Throwable throwable) {
                Log.e("ORDERS", throwable.getMessage());
            }
        });
    }

    public static void uploadImageToFirebase(Uri uri, String imagePath) {
        // Create "images" folder reference in Firebase Storage
        storageRef.child(imagePath);
        StorageReference imageRef = storageRef.child(imagePath);

        // Upload image to Firebase Storage
        UploadTask uploadTask = imageRef.putFile(uri);
        uploadTask.addOnSuccessListener(v -> {
            Log.d("CHECKPOINT", "Image uploaded successfully!");
            imageRef.getDownloadUrl().addOnSuccessListener(taskSnapshot -> {
                Log.d("CHECKPOINT", "Download URL: " + uri);
            }).addOnFailureListener(e -> {
                Log.e("CHECKPOINT", "Error uploading image: " + e.getMessage());
            });
        });
    }

    public static void downloadImageFromFirebase(Context context, ImageView imageView, String imagePath) {
        // Create reference to the image:
        StorageReference imageRef = storageRef.child(imagePath);
        Log.d("CHECKPOINT", imageRef.getDownloadUrl().toString());

        // Download image from Firebase Storage
        imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    if (context instanceof Activity && !((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        Log.d("CHECKPOINT", "Download URL: " + uri.toString());
                        // Use the download URL to display the image
                        Glide.with(context)
                                .load(uri)
                                .override(700, 700) // Resize the image to 500x500 pixels
                                .into(imageView);
                    } else {
                        Log.w("AndroidUtil", "Activity is no longer valid, cannot load image");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("CHECKPOINT", "Error downloading image: " + e.getMessage());
                    if (imagePath.contains("ProfilePic")) {
                        imageView.setImageResource(R.drawable.pfp_placeholder_icon);
                    } else {
                        imageView.setImageResource(R.drawable.eden_logotipo_3);
                    }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("TAG", "downloadImageFromFirebase: Deu bom");
                    } else {
                        Log.i("TAG", "downloadImageFromFirebase: Q porra ta acontecendo");

                    }
                });
        Log.d("CHECKPOINT", "where da fuck is the error starting?");
    }

    public static void downloadImageFromFirebaseWithRoundedCorners(Context context, ImageView imageView, String imagePath) {
        // Create reference to the image
        StorageReference imageRef = storageRef.child(imagePath);

        // Download image from Firebase Storage
        imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    if (context instanceof Activity && !((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        Log.d("CHECKPOINT", "Download URL: " + uri.toString());
                        // Use the download URL to display the image
                        Glide.with(context)
                                .load(uri)
                                .override(500, 500) // Resize the image to 300x300 pixels
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                                .into(imageView);
                    } else {
                        Log.w("AndroidUtil", "Activity is no longer valid, cannot load image");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("CHECKPOINT", "Error downloading image: " + e.getMessage());
                });
    }

    public static void uploadProfilePicToFirebase(Uri uri) {
        uploadImageToFirebase(uri, "profiles/ProfilePic_" + currentUser.getId() + ".jpg");
    }
    public static void downloadProfilePicFromFirebase(Context context, ImageView imageView) {
        downloadImageFromFirebase(context, imageView, "profiles/ProfilePic_" + currentUser.getId() + ".jpg");
    }
    public static void downloadOtherUserProfilePicFromFirebase(Context context, ImageView imageView, String userId) {
        downloadImageFromFirebase(context, imageView, "profiles/ProfilePic_" + userId + ".jpg");
    }

    public static boolean isImageFromCamera(Uri uri) {
        String scheme = uri.getScheme();
        return scheme != null && scheme.equals("file") && uri.getPath() != null && uri.getPath().contains("camera");
    }

    public static void getToken() {
        Retrofit client = RetrofitClient.getClient();
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        UserService service = client.create(UserService.class);
        Call<Token> call = service.getToken(new TokenRequest(email));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    token = response.body().getToken();
                    getUser(response1 -> {});
                    Log.d("TOKEN", "Token: " + token);
                } else {
                    Log.d("TOKEN", "Error getting token");
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Log.d("TOKEN", throwable.getMessage());
            }
        });
    }
    public static void getUser(UserCallback callback) {
        // TODO: Implementar callback no resto :cry:
        UserService service = RetrofitClient.getClientWithToken().create(UserService.class);
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Call<UserSchema> call = service.getParam(email);

        call.enqueue(new Callback<UserSchema>() {
            @Override
            public void onResponse(Call<UserSchema> call, Response<UserSchema> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    UserSchema user = response.body();
                    Log.d("USER", "User: " + currentUser.toString());
                    callback.setResponse(currentUser);
                } else {
                    try {
                        Log.d("ErrorBody", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSchema> call, Throwable throwable) {
                Log.d("CHECKPOINT", throwable.getMessage());
                callback.setResponse(null);
            }
        });
    }

    public static void authenticate(Context context) {
        // Getting token
        Retrofit client = RetrofitClient.getClient();
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        UserService service = client.create(UserService.class);
        Call<Token> call = service.getToken(new TokenRequest(email));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    token = response.body().getToken();

                    // GetUser
                    UserService service = RetrofitClient.getClientWithToken().create(UserService.class);
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Call<UserSchema> userCall = service.getParam(email);
                    userCall.enqueue(new Callback<UserSchema>() {
                        @Override
                        public void onResponse(Call<UserSchema> call, Response<UserSchema> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                currentUser = response.body();

                                openActivity(context, MainActivity.class);
                                ((Activity) context).finish();

                                Log.d("USER", "User: " + currentUser.toString());
                            } else {
                                try {
                                    Log.d("ErrorBody", response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserSchema> call, Throwable throwable) {
                            Log.d("CHECKPOINT", throwable.getMessage());
                        }
                    });

                } else if (response.code() == 503) {
                    Toast.makeText(context, "A api ainda nn acordou", Toast.LENGTH_SHORT).show();
                    authenticate(context);
                } else {
                    Log.d("TOKEN", "Error getting token");
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Log.d("TOKEN", throwable.getMessage());
                authenticate(context);
            }
        });

    }

}
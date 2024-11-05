package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;
import static com.eden.utils.AndroidUtil.favorites;
import static com.eden.utils.AndroidUtil.fetchFavorites;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CartItemResponse;
import com.eden.api.dto.FavoriteRequest;
import com.eden.api.dto.UserResponse;
import com.eden.api.services.CartService;
import com.eden.api.services.UserService;
import com.eden.model.Cart;
import com.eden.model.Product;
import com.eden.utils.NotificationReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProduct extends AppCompatActivity {

    FloatingActionButton btnFavorite;
    private Intent intent;
    private int productId;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        TextView productTitle = findViewById(R.id.product_title);
        TextView productPrice = findViewById(R.id.product_price);
        ImageView productImage = findViewById(R.id.product_image);
        TextView productDescription = findViewById(R.id.product_description);
        TextView productDelivery = findViewById(R.id.product_delivery);
        Button btnComprar = findViewById(R.id.button_buy_now);
        Button btnAdcCart = findViewById(R.id.button_add_cart);
        btnFavorite = findViewById(R.id.btn_favorite);

        intent = getIntent();

        // Getting intent extra
        productId = intent.getIntExtra("id", 0);

        // Set product image
        downloadImageFromFirebase(this, productImage, "product_" + productId + ".jpg");

        // Set product info
        productTitle.setText(intent.getStringExtra("nome"));
        productPrice.setText(String.format("R$ %.2f", intent.getFloatExtra("valor", 0.0f)));
        Log.d("valor", "valor: " + intent.getFloatExtra("valor", 0.0f));
        productDescription.setText(intent.getStringExtra("descricao"));
        productDelivery.setText(intent.getStringExtra("tipoEntrega"));

        // Check if its favorite
        for (Product product : favorites) {
            if (product.getId() == productId) {
                isFavorite = true;
                btnFavorite.setImageResource(R.drawable.heart_selected_icon);
                btnFavorite.setImageTintList(ColorStateList.valueOf(Color.CYAN));
            }
        }

        // Buy the product
        btnComprar.setOnClickListener(v -> {
            openActivity(this, CartActivity.class);
            addCart(intent.getIntExtra("id", 0));
//            notificar();
            finish();
        });

        // Add to cart
        btnAdcCart.setOnClickListener(v -> {
            addCart(intent.getIntExtra("id", 0));
        });

        btnFavorite.setOnClickListener(v -> {
            // Muda o ícone com animação
            if (!isFavorite) {
                addFavorite();
                btnFavorite.setImageResource(R.drawable.heart_selected_icon);
                btnFavorite.setImageTintList(ColorStateList.valueOf(Color.CYAN));
                btnFavorite.setScaleX(1.2f);
                btnFavorite.setScaleY(1.2f);
                isFavorite = true;
            } else {
                removeFavorite();
                btnFavorite.setImageResource(R.drawable.heart_add_icon);
                btnFavorite.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                isFavorite = false;
            }

            btnFavorite.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(100)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            btnFavorite.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();
                        }
                    })
                    .start();
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    private void addCart(int productId) {
        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<CartItemResponse> call = cartService.registerCart(new Cart(currentUser.getCartId(), productId));
        call.enqueue(new Callback<CartItemResponse>() {
            @Override
            public void onResponse(Call<CartItemResponse> call, Response<CartItemResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BuyProduct.this, "Produto adicionado ao carrinho com sucesso!", Toast.LENGTH_SHORT).show();
                    Log.d("CartSucess", response.body().toString());
                } else {
                    try {
                        if (response.errorBody().string().contains("Produto já cadastrado nesse carrinho")) {
                            Toast.makeText(BuyProduct.this, "Produto já cadastrado nesse carrinho", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // TODO: Tratar exceção
            }

            @Override
            public void onFailure(Call<CartItemResponse> call, Throwable throwable) {
                Toast.makeText(BuyProduct.this, throwable.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFavorite() {
        // TODO: Talvez fazer callback
        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);
        Call<UserResponse> call = userService.registerFavorite(new FavoriteRequest(currentUser.getId(), productId));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("FAVORITES", "inserido com sucesso");
                    fetchFavorites();
                } else {
                    try {
                        Log.d("FAVORITES", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                Log.d("FAVORITES", throwable.getMessage());
            }
        });

    }

    private void removeFavorite() {
        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);
        Call<Void> call = userService.deleteFavorite(String.valueOf(currentUser.getId()), String.valueOf(productId));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchFavorites();
                    Log.d("FAVORITES", "removido com sucesso");
                } else {
                    try {
                        Log.d("FAVORITES", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("FAVORITES", throwable.getMessage());
            }
        });

    }

//    public void notificar() {
//
//        // Criar Notificação
//        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
//        PendingIntent pendindIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
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
//        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;
import static com.eden.utils.AndroidUtil.getUser;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CartResponse;
import com.eden.api.services.CartService;
import com.eden.model.Cart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuyProduct extends AppCompatActivity {
    boolean isFavorite = false;
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
        FloatingActionButton btnFavorite = findViewById(R.id.btn_favorite);

        Intent intent = getIntent();

        downloadImageFromFirebase(this, productImage, "product_" + intent.getIntExtra("id", 0) + ".jpg");

        if (intent != null) {
            productTitle.setText(intent.getStringExtra("nome"));
            productPrice.setText(String.format("R$ %.2f", intent.getFloatExtra("valor", 0.0f)));
            Log.d("valor", "valor: " + intent.getFloatExtra("valor", 0.0f));
            productDescription.setText(intent.getStringExtra("descricao"));
            productDelivery.setText(intent.getStringExtra("tipoEntrega"));
        }

        btnComprar.setOnClickListener(v -> {
            openActivity(this, CartActivity.class);
            addCart(intent.getIntExtra("id", 0));
            notificar();
            finish();
        });

        btnAdcCart.setOnClickListener(v -> {
            addCart(intent.getIntExtra("id", 0));
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;

                // Muda o ícone com animação
                if (isFavorite) {
                    btnFavorite.setImageResource(R.drawable.heart_selected_icon);
                    btnFavorite.setImageTintList(ColorStateList.valueOf(Color.RED));
                    btnFavorite.setScaleX(1.2f);
                    btnFavorite.setScaleY(1.2f);
                } else {
                    btnFavorite.setImageResource(R.drawable.heart_add_icon);
                    btnFavorite.setImageTintList(ColorStateList.valueOf(Color.WHITE));
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
            }
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    private void addCart(int productId) {
        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<CartResponse> call = cartService.registerCart(new Cart(currentUser.getCartId(), productId));
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
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
            public void onFailure(Call<CartResponse> call, Throwable throwable) {
                Toast.makeText(BuyProduct.this, throwable.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void notificar() {

        // Criar Notificação
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendindIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.eden_logotipo_2)
                .setContentTitle("Título da notificação")
                .setContentText("CLIQUE E RECEBA!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendindIntent);

        // Criar Canal de Notificação
        NotificationChannel channel = new NotificationChannel("channel_id", "Notificar",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Mostrar notificação
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationCompat.notify(1, builder.build());


    }
}
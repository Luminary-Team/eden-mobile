package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;
import static com.eden.utils.AndroidUtil.getUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CartResponse;
import com.eden.api.services.CartService;
import com.eden.model.Cart;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuyProduct extends AppCompatActivity {
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
            notificar();
        });

        btnAdcCart.setOnClickListener(v -> {
            addCart(intent.getIntExtra("id", 0));
        });

    }

    private void getProduto(int id) {
//        Call<Product>
    }

    private void addCart(int productId) {
        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        Call<CartResponse> call = cartService.registerCart(new Cart(currentUser.getId(), productId));
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                // TODO: Tratar exceção
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable throwable) {

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
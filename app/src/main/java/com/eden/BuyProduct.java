package com.eden;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

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
import android.telecom.Call;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eden.model.Product;
import com.eden.utils.AndroidUtil;

public class BuyProduct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        TextView productTitle = findViewById(R.id.product_title);
        TextView product_price = findViewById(R.id.product_price);
        ImageView product_image = findViewById(R.id.product_image);
        TextView product_description = findViewById(R.id.product_description);
        TextView product_delivery = findViewById(R.id.product_delivery);
        Button btnComprar = findViewById(R.id.button_buy_now);

        Intent intent = getIntent();

        downloadImageFromFirebase(this, product_image, "product_" + intent.getIntExtra("id", 0) + ".jpg");

        if (intent != null) {
            productTitle.setText(intent.getStringExtra("nome"));
            product_price.setText("R$ " + intent.getStringExtra("valor"));
            product_description.setText(intent.getStringExtra("descricao"));
            product_delivery.setText(intent.getStringExtra("tipoEntrega"));
        }

        btnComprar.setOnClickListener(v -> {
            notificar();
        });

    }

    private void getProduto(int id) {
//        Call<Product>
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
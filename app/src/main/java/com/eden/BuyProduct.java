package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

        Intent intent = getIntent();
        if (intent != null) {
            productTitle.setText(intent.getStringExtra("nome"));
            product_price.setText("R$ " + intent.getStringExtra("valor"));
            product_description.setText(intent.getStringExtra("descricao"));
            product_delivery.setText(intent.getStringExtra("tipoEntrega"));
        } else {
            product_image.setImageResource(intent.getIntExtra("imagem", 0));
        }
    }
}
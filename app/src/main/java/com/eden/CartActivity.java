package com.eden;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class CartActivity extends AppCompatActivity {
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(v -> finish());

    }
}
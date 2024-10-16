package com.eden.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.eden.R;

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
package com.eden.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.eden.R;

public class BoostedProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boosted_product);

        findViewById(R.id.btn_continue_premium).setOnClickListener(v -> finish());

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}
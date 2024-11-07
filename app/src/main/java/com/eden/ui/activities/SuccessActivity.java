package com.eden.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eden.R;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);

        TextView successText = findViewById(R.id.success_text);
        Button btnContinue = findViewById(R.id.btn_continue);

        Intent intent = getIntent();
        String successType = intent.getStringExtra("successType");

        if (successType.equals("order")) {
            successText.setText("Pedido realizado com sucesso!");
        } else {
            successText.setText("Produto registrado com sucesso!");
        }

        Handler handler = new Handler();
        handler.postDelayed(this::finish, 5000);

        btnContinue.setOnClickListener(v -> finish());

    }
}
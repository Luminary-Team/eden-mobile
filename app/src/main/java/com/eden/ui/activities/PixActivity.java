package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.OrderService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pix);

        Button btnProceed = findViewById(R.id.btn_pix_finish_order);
        ImageView image = findViewById(R.id.imageView_qr_code_pix);

        Glide.with(this).load("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Link_pra_pagina_principal_da_Wikipedia-PT_em_codigo_QR_b.svg/1200px-Link_pra_pagina_principal_da_Wikipedia-PT_em_codigo_QR_b.svg.png").into(image);

        btnProceed.setOnClickListener(v -> finishOrder());

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    private void finishOrder() {

        // Finish the order
        OrderRequest orderRequest = new OrderRequest(currentUser.getCartId(), 1, "address");

        OrderService orderService = RetrofitClient.getClient().create(OrderService.class);
        Call<OrderResponse> orderServiceCall = orderService.registerOrder(orderRequest);
        orderServiceCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() ) {
                    // TODO: Card to confirm purchase
                    openActivity(PixActivity.this, MainActivity.class);
                } else {
                    // TODO: Handle exception
                    try {
                        Toast.makeText(PixActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (
                            IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable throwable) {

            }
        });
    }

}
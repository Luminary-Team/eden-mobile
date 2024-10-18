package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.OrderService;
import com.eden.model.PaymentType;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditCardInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_info);

        findViewById(R.id.btn_prosseguir).setOnClickListener(v -> {

            OrderRequest orderRequest = new OrderRequest(currentUser.getCartId(), 1, "address");

            OrderService orderService = RetrofitClient.getClient().create(OrderService.class);
            Call<OrderResponse> orderServiceCall = orderService.registerOrder(orderRequest);
            orderServiceCall.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() ) {
                        // TODO: Card para confirmar compra
                        openActivity(CreditCardInfo.this, MainActivity.class);
                    } else {
                        // TODO: Tratar exceção
                        try {
                            Toast.makeText(CreditCardInfo.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable throwable) {

                }
            });
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}
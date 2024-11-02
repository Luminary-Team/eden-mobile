package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CardResponse;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.CardService;
import com.eden.api.services.OrderService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_type);

        Button btnFinishOrder = findViewById(R.id.btn_finish_order);
//        RadioGroup radioButton = findViewById(R.id.paymentMethodRadioGroup);

        btnFinishOrder.setOnClickListener(v -> {

//            // For credit
//            if (radioButton.getCheckedRadioButtonId() == R.id.radio_credit) {
//
//            } else if (radioButton.getCheckedRadioButtonId() == R.id.radio_debit) {
//
//            } else if (radioButton.getCheckedRadioButtonId() == R.id.radio_pix) {
//
//            } else {
//                Toast.makeText(this, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
//            }

            CardService cardService = RetrofitClient.getClient().create(CardService.class);
            Call<List<CardResponse>> cardCall = cardService.getCardByUserId(currentUser.getId());
            cardCall.enqueue(new Callback<List<CardResponse>>() {
                @Override
                public void onResponse(Call<List<CardResponse>> call, Response<List<CardResponse>> response) {
                    if (response.isSuccessful()) {
                        List<CardResponse> card = response.body();
                        // Remains the card selected if the userCard's already registered
                        if (card != null) {

                            // Finish the order
                            OrderRequest orderRequest = new OrderRequest(currentUser.getCartId(), 1, "address");

                            OrderService orderService = RetrofitClient.getClient().create(OrderService.class);
                            Call<OrderResponse> orderServiceCall = orderService.registerOrder(orderRequest);
                            orderServiceCall.enqueue(new Callback<OrderResponse>() {
                                @Override
                                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                    if (response.isSuccessful() ) {
                                        // TODO: Card to confirm purchase
                                        openActivity(PaymentType.this, MainActivity.class);
                                    } else {
                                        // TODO: Handle exception
                                        try {
                                            Toast.makeText(PaymentType.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
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

                        } else {
                            openActivity(PaymentType.this, CreditCardInfo.class);
                        }
                    } else {
                        try {
                            Log.e("ErrorBody", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<CardResponse>> call, Throwable throwable) {

                }
            });

        });

    }
}
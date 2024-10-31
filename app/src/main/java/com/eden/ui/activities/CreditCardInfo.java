package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CardRequestSchema;
import com.eden.api.dto.CartResponse;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.CardService;
import com.eden.api.services.CartService;
import com.eden.api.services.OrderService;
import com.eden.model.Card;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreditCardInfo extends AppCompatActivity {

    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_credit_card_info);

        EditText cardNumber = findViewById(R.id.numero_cartao);
        EditText cvv = findViewById(R.id.cvv);
        EditText dataValidade = findViewById(R.id.data_validade);
        EditText cep = findViewById(R.id.cep);
        Button btnProsseguir = findViewById(R.id.btn_prosseguir);
        total = findViewById(R.id.textView_total);

        setTotal();

        // Save card info
        btnProsseguir.setOnClickListener(card -> {

            CardRequestSchema cardRequestSchema = new CardRequestSchema(
                    currentUser.getId(),
                    cardNumber.getText().toString().replace(" ", ""),
                    cvv.getText().toString(),
                    cvv.getText().toString(),
                    dataValidade.getText().toString()
            );

            CardService cardService = RetrofitClient.getClient().create(CardService.class);
            Call<Card> call = cardService.createCard(cardRequestSchema);
            call.enqueue(new Callback<Card>() {
                @Override
                public void onResponse(Call<Card> call, Response<Card> response) {
                    if (response.isSuccessful()) {
                        // Show dialog to confirm purchase
                        new AlertDialog.Builder(CreditCardInfo.this)
                                .setTitle("Purchase Confirmation")
                                .setMessage("Your card has been saved successfully. Do you want to proceed with the purchase?")
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    // Finish the order
                                    OrderRequest orderRequest = new OrderRequest(currentUser.getCartId(), 1, "address");

                                    OrderService orderService = RetrofitClient.getClient().create(OrderService.class);
                                    Call<OrderResponse> orderServiceCall = orderService.registerOrder(orderRequest);
                                    orderServiceCall.enqueue(new Callback<OrderResponse>() {
                                        @Override
                                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                            if (response.isSuccessful() ) {
                                                // TODO: Card to confirm purchase
                                                openActivity(CreditCardInfo.this, MainActivity.class);
                                            } else {
                                                // TODO: Handle exception
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

                                })
                                .setNegativeButton("No", null)
                                .show();

                    } else {
                        // Handle exception
                        try {
                            Toast.makeText(CreditCardInfo.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Card> call, Throwable throwable) {
                    Toast.makeText(CreditCardInfo.this, "Failed to save card info: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        // Format Card Number
        cardNumber.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada antes da mudança
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada durante a mudança
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                    StringBuilder formatted = new StringBuilder();

                    for (int i = 0; i < clean.length(); i++) {
                        if (i > 0 && i % 4 == 0) {
                            formatted.append(" "); // Adiciona espaço a cada 4 dígitos
                        }
                        formatted.append(clean.charAt(i));
                    }
                    current = formatted.toString();
                    cardNumber.setText(current);
                    cardNumber.setSelection(current.length()); // Move o cursor para o final
                }
            }
        });

        // Format Expiration Date (MM/AA)
        dataValidade.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada antes da mudança
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada durante a mudança
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                    StringBuilder formatted = new StringBuilder();

                    for (int i = 0; i < clean.length(); i++) {
                        if (i == 2) {
                            formatted.append("/"); // Adiciona a barra após o mês
                        }
                        formatted.append(clean.charAt(i));
                    }
                    current = formatted.toString();
                    dataValidade.setText(current);
                    dataValidade.setSelection(current.length()); // Move o cursor para o final
                }
            }
        });

        // Format CEP (00000-000)
        cep.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada antes da mudança
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada durante a mudança
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                    StringBuilder formatted = new StringBuilder();

                    for (int i = 0; i < clean.length(); i++) {
                        if (i == 5) {
                            formatted.append("-"); // Adiciona o hífen após os cinco primeiros dígitos
                        }
                        formatted.append(clean.charAt(i));
                    }
                    current = formatted.toString();
                    cep.setText(current);
                    cep.setSelection(current.length()); // Move o cursor para o final
                }
            }
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    private void setTotal() {
        CartService orderService = RetrofitClient.getClient().create(CartService.class);
        Call<CartResponse> call = orderService.getCartItemsByCartId(currentUser.getCartId());
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    CartResponse cartResponse = response.body();
                    total.setText("R$ " + String.format("%.2f", cartResponse.getTotalSale()));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable throwable) {

            }
        });
    }

}
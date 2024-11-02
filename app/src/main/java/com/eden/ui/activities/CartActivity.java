package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.getUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.R;
import com.eden.adapter.CartAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CardResponse;
import com.eden.api.dto.CartItemResponse;
import com.eden.api.dto.CartResponse;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.CardService;
import com.eden.api.services.CartService;
import com.eden.api.services.OrderService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ImageButton backBtn;
    ProgressBar progressBar;
    RelativeLayout emptyCart;

    @Override
    protected void onResume() {
        super.onResume();

        getUser(response -> {

            Button cartBtn = findViewById(R.id.btn_cart);

            TextView totalPrice = findViewById(R.id.textView_total);

            RecyclerView recyclerView = findViewById(R.id.cart_recyclerView);

            // Getting cart items and display them
            CartService cartService = RetrofitClient.getClient().create(CartService.class);

            Call<CartResponse> call = cartService.getCartItemsByCartId(currentUser.getCartId());
            call.enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body().getCartItems().size() != 0) {
                        // TODO: Tratar exceção
                        emptyCart.setVisibility(View.GONE);
                        CartResponse cartResponse = response.body();

                        // Find Total
                        totalPrice.setText(String.format("R$ %.2f", cartResponse.getTotalSale()));

                        // Find Cart Items
                        List<CartItemResponse> cartItemResponses = cartResponse.getCartItems();
                        recyclerView.setAdapter(new CartAdapter(cartItemResponses));

                        cartBtn.setOnClickListener(v -> {
                            openActivity(CartActivity.this, PaymentType.class);
                        });

                    } else {
                        emptyCart.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    emptyCart.setVisibility(View.VISIBLE);
                }

            });

            recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        backBtn = findViewById(R.id.back_btn);
        progressBar = findViewById(R.id.products_progressBar);
        emptyCart = findViewById(R.id.empty_cart);

        progressBar.setVisibility(View.VISIBLE);
        emptyCart.setVisibility(View.GONE);

        // Setting up back button
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

}
package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.eden.R;
import com.eden.adapter.CartAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CartResponse;
import com.eden.api.services.CartService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    List<CartResponse> cartResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageButton backBtn = findViewById(R.id.back_btn);
        ProgressBar progressBar = findViewById(R.id.products_progressBar);
        RecyclerView recyclerView = findViewById(R.id.cart_recyclerView);
        RelativeLayout emptyCart = findViewById(R.id.empty_cart);

        progressBar.setVisibility(View.VISIBLE);
        emptyCart.setVisibility(View.GONE);

        // Getting cart items and display them
        CartService cartService = RetrofitClient.getClient().create(CartService.class);
        // TODO: Entender pq não existe nada no current user
        Call<List<CartResponse>> call = cartService.getCartItemsByCartId(currentUser.getCartId());

        call.enqueue(new Callback<List<CartResponse>>() {
            @Override
            public void onResponse(Call<List<CartResponse>> call, Response<List<CartResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // TODO: Tratar exceção
                    emptyCart.setVisibility(View.GONE);
                    cartResponses = response.body();
                    recyclerView.setAdapter(new CartAdapter(cartResponses));
                }
                if (response.body() == null) {
                    emptyCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CartResponse>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                emptyCart.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting up back button
        backBtn.setOnClickListener(v -> finish());
    }
}
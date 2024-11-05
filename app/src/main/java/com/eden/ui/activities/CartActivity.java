package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.getUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.eden.callbacks.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ImageButton backBtn;
    ProgressBar progressBar;
    RelativeLayout emptyCart;
    CartAdapter adapter;

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

    @Override
    protected void onResume() {
        super.onResume();

        getUser(response -> {

            Button cartBtn = findViewById(R.id.btn_cart);
            TextView changeBtn = findViewById(R.id.textView_payment_type);
            TextView totalPrice = findViewById(R.id.textView_total);
            RecyclerView recyclerView = findViewById(R.id.cart_recyclerView);

            changeBtn.setOnClickListener(v -> {
                changePaymentType();
            });

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
                        adapter = new CartAdapter(cartItemResponses);

                        recyclerView.setAdapter(adapter);

                        enableSwipeToDelete(adapter, recyclerView);

                        cartBtn.setOnClickListener(v -> {
                            finishOrder();
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

    private void changePaymentType() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_payment_type);

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
                    openActivity(CartActivity.this, MainActivity.class);
                } else {
                    // TODO: Handle exception
                    try {
                        Toast.makeText(CartActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
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

    private void enableSwipeToDelete(CartAdapter adapter, RecyclerView recyclerView) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final CartItemResponse item = adapter.getData().get(position);

                // TODO: Remover do orderService
                CartService cartService = RetrofitClient.getClient().create(CartService.class);
                Call<Void> call = cartService.deleteCartItem(item.getCartItemId(), currentUser.getCartId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(CartActivity.this, "Produto removido", Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) {
                            Log.e("DELETE", response.body().toString());
                        } else {
                            try {
                                Log.e("DELETE", response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(CartActivity.this, "Produto não removido", Toast.LENGTH_SHORT).show();
                        Log.e("DELETE", throwable.getMessage().toString());
                    }
                });
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

    }
}
package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eden.R;
import com.eden.adapter.CartAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CartItemResponse;
import com.eden.api.dto.CartResponse;
import com.eden.api.dto.OrderRequest;
import com.eden.api.dto.OrderResponse;
import com.eden.api.services.CartService;
import com.eden.api.services.OrderService;
import com.eden.utils.callbacks.SwipeToDeleteCallback;

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
    RadioGroup radioGroupPayment;
    RadioButton radioPix, radioCredit;
    Dialog dialog;
    String selectedPayment;
    Button cartBtn;
    TextView totalPrice, paymentType;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_payment_type);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        radioGroupPayment = dialog.findViewById(R.id.radio_group_payment);
        radioGroupPayment.check(R.id.radio_pix);

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

        cartBtn = findViewById(R.id.btn_cart);
        TextView changeBtn = findViewById(R.id.textView_change_payment_type);
        ImageView paymentTypeImage = findViewById(R.id.imageView_payment_type);
        paymentType = findViewById(R.id.textView_payment_type);
        totalPrice = findViewById(R.id.textView_total);
        recyclerView = findViewById(R.id.cart_recyclerView);

        selectedPayment = "Pix";
        paymentType.setText(selectedPayment);

        Glide.with(this).load("https://img.icons8.com/color/200/pix.png").into(paymentTypeImage);

        changeBtn.setOnClickListener(v -> changePaymentType(paymentType, paymentTypeImage));

        loadCart();

        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

    }

    private void loadCart() {
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

                    enableSwipeToDelete();

                    cartBtn.setOnClickListener(v -> {
                        if (radioGroupPayment.getCheckedRadioButtonId() == R.id.radio_pix) {
                            openActivity(CartActivity.this, PixActivity.class);
                        } else {
                            if (currentUser.getCartId() != 0) {
                                finishOrder();
                            } else {
                                openActivity(CartActivity.this, CreditCardInfo.class);
                            }
                        }
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

    }

    private void changePaymentType(TextView paymentType, ImageView paymentTypeImage) {

        // Botão de fechar
        ImageView btnClose = dialog.findViewById(R.id.back_btn);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        TextView btnChangeCreditCard = dialog.findViewById(R.id.change_credit_card);
        btnChangeCreditCard.setOnClickListener(v -> openActivity(CartActivity.this, CreditCardInfo.class));

        // RadioGroup para opções de pagamento
        radioGroupPayment = dialog.findViewById(R.id.radio_group_payment);
        radioPix = dialog.findViewById(R.id.radio_pix);
        radioCredit = dialog.findViewById(R.id.radio_cartao);
        radioPix.setChecked(true);

        // Botão de confirmar
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            int selectedId = radioGroupPayment.getCheckedRadioButtonId();

            if (selectedId == R.id.radio_pix) {
                selectedPayment = "Pix";
                Glide.with(this).load("https://img.icons8.com/color/200/pix.png").into(paymentTypeImage);
            } else {
                selectedPayment = "Cartão";
                Glide.with(this).load("https://cdn-icons-png.flaticon.com/512/2695/2695971.png").into(paymentTypeImage);
            }

            // Atualiza o TextView com a forma de pagamento selecionada
            paymentType.setText(selectedPayment);
            dialog.dismiss();
        });

        dialog.show();
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
                    Intent intent = new Intent(CartActivity.this, SuccessActivity.class);
                    intent.putExtra("successType", "order");
                    intent.putExtra("orderId", response.body().getTotalValue());
                    startActivity(intent);
                    finish();
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

    private void enableSwipeToDelete() {
        // TODO: ARRUMAR ESSA PORRA
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final CartItemResponse item = adapter.getData().get(position);

                CartService cartService = RetrofitClient.getClient().create(CartService.class);
                Call<Void> call = cartService.deleteCartItem(item.getCartItemId(), currentUser.getCartId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        recyclerView.setAdapter(null);
                        loadCart();
                        Toast.makeText(CartActivity.this, "Produto removido", Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) {
                            Log.e("DELETE", "removido com sucesso");
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
package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.ProductAdapter;
import com.eden.adapter.UserProductAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.UserResponse;
import com.eden.api.services.UserService;
import com.eden.model.Product;
import com.eden.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView_favorites);

        getFavorites();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    public void getFavorites() {

        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);
        Call<UserResponse> call = userService.getFavorites(String.valueOf(currentUser.getId()));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    List<Product> favoriteProducts = userResponse.getProducts();
                    recyclerView.setAdapter(new UserProductAdapter(favoriteProducts));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {

            }
        });
    }
}
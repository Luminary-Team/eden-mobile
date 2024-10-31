package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.favorites;

import android.os.Bundle;
import android.util.Log;

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

import java.io.IOException;
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

        recyclerView.setAdapter(new UserProductAdapter(favorites));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

}
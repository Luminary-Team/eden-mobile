package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.favorites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.ProductAdapter;
import com.eden.adapter.ProductAdapterNoMenu;
import com.eden.adapter.UserProductAdapter;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView_favorites);

        recyclerView.setAdapter(new ProductAdapterNoMenu(favorites));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

}
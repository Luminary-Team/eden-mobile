package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.boughtProducts;
import static com.eden.utils.AndroidUtil.favorites;
import static com.eden.utils.AndroidUtil.fetchBoughtProducts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.UserProductAdapter;

public class BoughtProducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bought_products);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_bought_products);

        recyclerView.setAdapter(new UserProductAdapter(boughtProducts));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}
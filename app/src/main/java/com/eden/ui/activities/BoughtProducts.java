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
import com.eden.adapter.ProductAdapterNoMenu;
import com.eden.adapter.UserProductAdapter;

public class BoughtProducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_products);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_bought_products);

        if (!boughtProducts.isEmpty()) {
            recyclerView.setAdapter(new ProductAdapterNoMenu(boughtProducts));
        } else {
            // Todo: Imagem bonitinha de vazio
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}
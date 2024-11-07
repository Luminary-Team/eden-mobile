package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.ProductPremiumAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.services.ProductService;
import com.eden.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PremiumProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_product);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_premium_product);

        ProductService productService = RetrofitClient.getClient().create(ProductService.class);
        Call<List<Product>> call = productService.getPremiumProducts(currentUser.getId());
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();

                if (products != null) {
                    recyclerView.setAdapter(new ProductPremiumAdapter(products));
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {

            }
        });

    }

}
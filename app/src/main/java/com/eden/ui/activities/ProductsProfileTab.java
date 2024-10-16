package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eden.R;
import com.eden.adapter.ProductAdapter;
import com.eden.adapter.UserProductAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.services.ProductService;
import com.eden.model.Product;
import com.eden.utils.AndroidUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsProfileTab extends Fragment {

    private List<Product> products = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_profile_tab, container, false);

        ProgressBar progressBar = view.findViewById(R.id.products_user_progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_products_user);


        progressBar.setVisibility(View.VISIBLE);

        ProductService productService = RetrofitClient.getClient().create(ProductService.class);
        Call<List<Product>> call = productService.getProductsByUserId(currentUser.getId());
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    products = response.body();
                    recyclerView.setAdapter(new UserProductAdapter(products));
                } else {
                    try {
                        Log.d("ErrorBody", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                throwable.printStackTrace();
            }
        });

        return view;
    }
}
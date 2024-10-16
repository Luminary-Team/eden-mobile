package com.eden.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eden.R;
import com.eden.adapter.ProductAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.services.ProductService;
import com.eden.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    List<Product> products = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ProgressBar progressBar = view.findViewById(R.id.products_progressBar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_produtos);

        progressBar.setVisibility(View.VISIBLE);

        ProductService productService = RetrofitClient.getClient().create(ProductService.class);

        // TODO: Notify no Adapter quando for registrado

        Call<List<Product>> call = productService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    products = response.body();
                    recyclerView.setAdapter(new ProductAdapter(products));
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

        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        return view;
    }

}
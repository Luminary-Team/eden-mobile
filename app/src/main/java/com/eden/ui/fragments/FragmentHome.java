package com.eden.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        EditText searchBar = view.findViewById(R.id.search_bar);
        ProgressBar progressBar = view.findViewById(R.id.products_progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_produtos);

        progressBar.setVisibility(View.VISIBLE);

        searchBar.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerView.setAdapter(null);
                progressBar.setVisibility(View.VISIBLE);
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                runnable = () -> {
                    String query = "%25on%25";
                    if (s != null) {
                        ProductService productService = RetrofitClient.getClient().create(ProductService.class);
                        Call<List<Product>> call = productService.getProductByTitle(query);
                        call.enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("Product", response.body().toString());
                                        recyclerView.setAdapter(new ProductAdapter(products));
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(view.getContext(), "Nenhum resultado encontrado", Toast.LENGTH_SHORT).show();
                                    }
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
                    }
                };
                handler.postDelayed(runnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Getting products and display them
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
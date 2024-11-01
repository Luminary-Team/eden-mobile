package com.eden.ui.fragments;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.fetchBoughtProducts;
import static com.eden.utils.AndroidUtil.fetchFavorites;
import static com.eden.utils.AndroidUtil.getUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    List<Product> premiumProducts = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar = view.findViewById(R.id.search_bar);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_home);
        progressBar = view.findViewById(R.id.products_progressBar);
        recyclerView = view.findViewById(R.id.recyclerView_produtos);

        // Configura o RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Se for a posição 0 (carrossel), ocupa 2 colunas
                if (position == 0) {
                    return 2; // Ocupa 2 colunas
                }
                return 1; // Os outros itens ocupam 1 coluna
            }
        });

        getUser(response -> {
            if (response != null) {
                // Chama os produtos ao carregar a tela
                loadProducts(recyclerView, progressBar);

                // Search for Products
                searchProducts(searchBar, recyclerView, progressBar);

                // Get Favorites
                fetchFavorites();

                // Get Bought Products
                fetchBoughtProducts();
            }
        });

        // Reload posts on refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setAdapter(null);
            loadProducts(recyclerView, progressBar);
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void loadProducts(RecyclerView recyclerView, ProgressBar progressBar) {
        // Get all products
        progressBar.setVisibility(View.VISIBLE);
        ProductService productService = RetrofitClient.getClient().create(ProductService.class);
        if (currentUser != null) {
            Call<List<Product>> callProduct = productService.getAllProducts(currentUser.getId());
            callProduct.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {

                        products = response.body();

                        Call<List<Product>> callPremium = productService.getPremiumProducts(currentUser.getId());
                        callPremium.enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                if (response.isSuccessful()) {
                                    premiumProducts = response.body();

                                    recyclerView.setAdapter(new ProductAdapter(products, premiumProducts));

                                }
                            }

                            @Override
                            public void onFailure(Call<List<Product>> call, Throwable throwable) {

                            }
                        });

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
    }

    private void searchProducts(EditText searchBar, RecyclerView recyclerView, ProgressBar progressBar) {

        if (currentUser != null) {
            // Search for Products
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
                        String query = "%" + s.toString() + "%"; // Atualize a query de busca com a string digitada pelo usuário
                        if (!query.contentEquals("%%")) {
                            ProductService productService = RetrofitClient.getClient().create(ProductService.class);
                            Call<List<Product>> call = productService.getProductByTitle(query); // Atualize a chamada da API com a query de busca
                            call.enqueue(new Callback<List<Product>>() {
                                @Override
                                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            products = response.body(); // Atualize a lista de produtos com os resultados da busca
                                            recyclerView.setAdapter(new ProductAdapter(products, premiumProducts)); // Notifique o adapter sobre a atualização da lista de produtos
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getActivity(), "Nenhum resultado encontrado", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
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
                        } else {
                            loadProducts(recyclerView, progressBar);
                        }
                    };
                    handler.postDelayed(runnable, 500);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

}
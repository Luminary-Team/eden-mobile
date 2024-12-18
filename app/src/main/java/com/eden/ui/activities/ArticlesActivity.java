package com.eden.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.ArticlesAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.services.ArticlesService;
import com.eden.model.Article;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArticlesActivity extends AppCompatActivity {

    List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        ImageButton backBtn = findViewById(R.id.back_btn);
        ProgressBar progressBar = findViewById(R.id.products_progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_articles);

        progressBar.setVisibility(View.VISIBLE);

        ArticlesService service = RetrofitClient.getClientMongo().create(ArticlesService.class);
        Call<List<Article>> call = service.getArticles();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    articles = response.body();
                    recyclerView.setAdapter(new ArticlesAdapter(articles));
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                throwable.getMessage();
            }
        });

        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // Setting up back button
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }
}
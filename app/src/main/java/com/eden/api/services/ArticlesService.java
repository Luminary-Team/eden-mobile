package com.eden.api.services;

import com.eden.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticlesService {

    @GET("/news")
    Call<List<Article>> getArticles();

}

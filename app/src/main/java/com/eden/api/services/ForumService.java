package com.eden.api.services;

import com.eden.api.dto.PostResponse;
import com.eden.api.dto.PostResponseMongo;
import com.eden.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ForumService {

    @GET("/forum")
    Call<List<PostResponse>> getPosts();

    @POST("/forum")
    Call<Post> createPost(@Body Post post);

}

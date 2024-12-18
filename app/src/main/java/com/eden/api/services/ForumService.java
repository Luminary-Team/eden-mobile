package com.eden.api.services;

import com.eden.api.dto.CommentRequest;
import com.eden.api.dto.PostResponse;
import com.eden.api.dto.PostResponseMongo;
import com.eden.model.Post;
import com.eden.model.PostRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ForumService {

    @GET("/forum")
    Call<List<PostResponse>> getPosts(); // Get all posts from sql api

    @GET("/forum")
    Call<List<PostResponse>> getUserPosts(@Query("id") int id);

    @POST("/forum/comment/{postId}")
    Call<PostResponseMongo> addComment(@Path("postId") String postId, @Body CommentRequest commentRequest); // Add comment to post in mongodb api

    @POST("/forum/like/{forumId}")
    Call<PostResponseMongo> likePost(@Path("forumId") String forumId, @Body int engagerId);

    @POST("/forum")
    Call<PostResponse> createPost(@Body PostRequest postRequest); // Create new post in mongodb api

}

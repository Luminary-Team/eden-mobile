package com.eden.ui.tabs;

import static com.eden.utils.AndroidUtil.currentUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eden.R;
import com.eden.adapter.PostAdapter;
import com.eden.adapter.UserProductAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.PostResponse;
import com.eden.api.services.ForumService;
import com.eden.model.Post;
import com.eden.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsProfileTab extends Fragment {

    private List<PostResponse> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_profile_tab, container, false);

        ProgressBar progressBar = view.findViewById(R.id.products_user_progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts_user);

        progressBar.setVisibility(View.VISIBLE);

        ForumService forumService = RetrofitClient.getClient().create(ForumService.class);
        Call<List<PostResponse>> call = forumService.getUserPosts(currentUser.getId());
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    posts = response.body();
                    recyclerView.setAdapter(new PostAdapter(posts));
                } else {
                    try {
                        Log.d("ErrorBody", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                throwable.printStackTrace();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
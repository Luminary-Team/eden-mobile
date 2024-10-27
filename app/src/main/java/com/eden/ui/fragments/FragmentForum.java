package com.eden.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.PostAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.services.ForumService;
import com.eden.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForum extends Fragment {

    public static FragmentForum newInstance() {
         return new FragmentForum();
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        List<Post> forumList = new ArrayList<>();

        forumList.add(new Post("Lorem Ipsum 1"));
        forumList.add(new Post("Lorem Ipsum 2"));
        forumList.add(new Post("Lorem Ipsum 3"));
        forumList.add(new Post("Lorem Ipsum 4"));
        forumList.add(new Post("Lorem Ipsum 5"));
        forumList.add(new Post("Lorem Ipsum 6"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts);

        recyclerView.setAdapter(new PostAdapter(forumList));

//        ForumService forumService = RetrofitClient.getClientMongo().create(ForumService.class);
//        Call<List<Post>> call = forumService.getPosts();
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                if (response.isSuccessful()) {
//                    List<Post> posts = response.body();
//                    recyclerView.setAdapter(new PostAdapter(posts));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable throwable) {
//                Log.d("ERROR", throwable.getMessage());
//            }
//        });

//        recyclerView.setAdapter(new PostAdapter(forumList));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
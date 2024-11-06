package com.eden.ui.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eden.R;
import com.eden.model.Product;

import java.util.ArrayList;
import java.util.List;

public class PostsProfileTab extends Fragment {

    private List<Product> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_profile_tab, container, false);

        ProgressBar progressBar = view.findViewById(R.id.products_user_progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts_user);

        progressBar.setVisibility(View.VISIBLE);

//        Forum

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
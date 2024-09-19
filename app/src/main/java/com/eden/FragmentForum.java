package com.eden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

//        List<FragmentForum> forumList = new ArrayList<>();
//        forumList.add(new FragmentForum());
//        forumList.add(new FragmentForum());
//        forumList.add(new FragmentForum());
//        forumList.add(new FragmentForum());
//
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts);
//
//        recyclerView.setAdapter(new ListAdapter() {
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            }
//        });

        return view;
    }
}
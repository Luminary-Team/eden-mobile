package com.eden.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.model.Post;

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

        List<Post> forumList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts);
        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_forum_layout, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView content = holder.itemView.findViewById(R.id.post_content);

                Post post = forumList.get(position);

                content.setText(post.getContent());
            }

            @Override
            public int getItemCount() {
                return forumList.size();
            }
        });

        return view;
    }
}
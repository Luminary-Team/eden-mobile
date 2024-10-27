package com.eden.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {
    private final List<Post> postList;

    public PostAdapter(List<Post> forumList) {
        this.postList = forumList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_forum_layout, parent, false);
        return new PostAdapter.ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolderPost holder, int position) {
        Post post = postList.get(position);
        holder.content.setText(post.getContent());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolderPost extends RecyclerView.ViewHolder {
        private TextView content;
        private ImageView userPfp;
        // TODO: Colocar coisas do header

        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);
            content  = itemView.findViewById(R.id.post_content);
        }
    }
}

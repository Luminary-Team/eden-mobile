package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadOtherUserProfilePicFromFirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolderComment> {
    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
        return new CommentAdapter.ViewHolderComment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment holder, int position) {
        Comment comment = commentList.get(position);

        // TODO: Falar pro tabuchi devolver comments
        holder.content.setText(comment.getContent());
        holder.userName.setText("Gus");
        holder.name.setText("Gustavo");
        downloadOtherUserProfilePicFromFirebase(holder.content.getContext(), holder.commentPfp, "6");

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolderComment extends RecyclerView.ViewHolder {
        private TextView content, userName, name;
        private ImageView commentPfp;

        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.comment_content);
            userName = itemView.findViewById(R.id.profile_username);
            name = itemView.findViewById(R.id.profile_name);
            commentPfp = itemView.findViewById(R.id.profile_pic);
        }
    }
}

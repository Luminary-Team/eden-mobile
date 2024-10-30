package com.eden.adapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.eden.utils.AndroidUtil.downloadOtherUserProfilePicFromFirebase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.api.dto.PostResponse;
import com.eden.model.Comment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {
    private final List<PostResponse> postList;

    public PostAdapter(List<PostResponse> postList) {
        Collections.reverse(postList);
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_forum_layout, parent, false);
        return new PostAdapter.ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolderPost holder, int position) {
        PostResponse post = postList.get(position);
        holder.content.setText(post.getContent());
        holder.name.setText(post.getUser().getName());
        holder.userName.setText(post.getUser().getUserName());
        downloadOtherUserProfilePicFromFirebase(holder.content.getContext(), holder.userPfp, String.valueOf(post.getUser().getId()));

        holder.itemView.setOnClickListener(v -> {
            List<Comment> comments = post.getComments();

            // TODO: Isso vai ter que ser no callback de getCommentsForPost
            openBottomSheet(holder.itemView.getContext(), comments);
        });
    }

    private void openBottomSheet(Context context, List<Comment> comments) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_comments, null);
        ProgressBar progressBar = dialogView.findViewById(R.id.comments_progressBar);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView_comments);

        // Set up the bottom sheet
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Show the bottom sheet
        bottomSheetDialog.show();

        progressBar.setVisibility(View.VISIBLE);

        if (comments != null && !comments.isEmpty()) {
            // Set up the RecyclerView
            recyclerView.setAdapter(new CommentAdapter(comments));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            progressBar.setVisibility(View.GONE);
        } else {
            // Set if there are no comments
            TextView noCommentsText = dialogView.findViewById(R.id.no_comments_text);
            noCommentsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

//        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                Toast.makeText(context, "Bottom sheet dismissed", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolderPost extends RecyclerView.ViewHolder {
        private TextView content, name, userName;
        private ImageView userPfp;
        // TODO: Colocar coisas do header

        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);
            content  = itemView.findViewById(R.id.post_content);
            userPfp = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.profile_name);
            userName = itemView.findViewById(R.id.profile_username);

        }
    }
}

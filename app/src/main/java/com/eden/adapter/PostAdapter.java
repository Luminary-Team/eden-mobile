package com.eden.adapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;
import static com.eden.utils.AndroidUtil.downloadOtherUserProfilePicFromFirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.CommentRequest;
import com.eden.api.dto.PostResponse;
import com.eden.api.dto.PostResponseMongo;
import com.eden.api.services.ForumService;
import com.eden.model.Comment;
import com.eden.ui.fragments.FragmentForum;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolderPost holder, int position) {
        // Set marginTop
        if (position == 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.topMargin = 20;
            holder.itemView.setLayoutParams(params);
        }

        PostResponse post = postList.get(position);
        holder.content.setText(post.getContent());
        holder.name.setText(post.getUser().getName());
        holder.userName.setText("@" + post.getUser().getUserName());
        downloadOtherUserProfilePicFromFirebase(holder.content.getContext(), holder.userPfp, String.valueOf(post.getUser().getId()));
        downloadImageFromFirebase(holder.content.getContext(), holder.imagePost, "post_" + post.getId() + ".jpg");

        holder.itemView.setOnClickListener(v -> {
            // TODO: Isso vai ter que ser no callback de getCommentsForPost
            openBottomSheet(holder.itemView.getContext(), post);
        });
    }

    private void openBottomSheet(Context context, PostResponse post) {
        List<Comment> comments = post.getComments();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_comments, null);
        ProgressBar progressBar = dialogView.findViewById(R.id.comments_progressBar);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView_comments);
        EditText editTextComments = dialogView.findViewById(R.id.editText_comments);
        ImageView sendButton = dialogView.findViewById(R.id.imageView_send);

        // Set up the bottom sheet
        bottomSheetDialog.setContentView(dialogView);
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

        sendButton.setOnClickListener(v -> {
            ForumService forumService = RetrofitClient.getClientMongo().create(ForumService.class);
            Call<PostResponseMongo> call = forumService.addComment(post.getId(),
                    new CommentRequest(currentUser .getId(), editTextComments.getText().toString()));

            call.enqueue(new Callback<PostResponseMongo>() {
                @Override
                public void onResponse(Call<PostResponseMongo> call, Response<PostResponseMongo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("COMMENTS", response.body().toString());

                        FragmentForum.loadPosts();

                        // TODO: Atualizar em tempo real
                        bottomSheetDialog.dismiss();
                    } else {
                        Log.d("COMMENTS", "Response not successful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<PostResponseMongo> call, Throwable throwable) {
                    Log.d("COMMENTS", throwable.getMessage());
                }
            });
        });

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
        private ImageView userPfp, imagePost;
        // TODO: Colocar coisas do header

        public ViewHolderPost(@NonNull View itemView) {
            super(itemView);
            content  = itemView.findViewById(R.id.post_content);
            userPfp = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.profile_name);
            userName = itemView.findViewById(R.id.profile_username);
            imagePost = itemView.findViewById(R.id.post_image);
        }
    }
}

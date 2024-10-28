package com.eden.adapter;

import static com.eden.utils.AndroidUtil.downloadOtherUserProfilePicFromFirebase;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.model.Comment;
import com.eden.model.Post;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {
    private final List<Post> postList;
    private static final List<Comment> comments = new ArrayList<>();

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
        holder.name.setText("Gustavo");
        holder.userName.setText("Gus");
        downloadOtherUserProfilePicFromFirebase(holder.content.getContext(), holder.userPfp, "6");

        holder.itemView.setOnClickListener(v -> {
            // Aqui você deve obter os comentários do post
            List<Comment> comments = getCommentsForPost(post); // Método fictício para obter os comentários

            // TODO: Isso vai ter que ser no callback de getCommentsForPost
            openBottomSheet(holder.itemView.getContext(), comments);
        });
    }

    private void openBottomSheet(Context context, List<Comment> comments) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_comments, null);

//        // Set rounded corners for the bottom sheet
//        bottomSheetDialog.getBehavior().setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
//        bottomSheetDialog.getWindow().findViewById(R.layout.bottom_sheet_comments)
//                .setBackgroundResource(R.drawable.rounded_bottom_sheet);

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CommentAdapter(comments));

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(context, "Bottom sheet dismissed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Comment> getCommentsForPost(Post post) {
        // TODO: Obter os comentários do post
        comments.add(new Comment("Olá, como você está?"));
        comments.add(new Comment("Olá, eu estou bem, obrigado! E você?"));
        comments.add(new Comment("Eu estou bem tambem, estou muito feliz em ter conhecido você!"));
        comments.add(new Comment("Eu tambem, você é muito agradavel!"));
        comments.add(new Comment("Obrigado, eu tambem acho você muito agradavel!"));
        return comments;
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

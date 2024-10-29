package com.eden.ui.fragments;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import static com.eden.utils.AndroidUtil.currentUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eden.R;
import com.eden.adapter.PostAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.PostResponse;
import com.eden.api.services.ForumService;
import com.eden.model.Comment;
import com.eden.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForum extends Fragment {

    List<PostResponse> postList = new ArrayList<>();

    public static FragmentForum newInstance() {
         return new FragmentForum();
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        FloatingActionButton addPost = view.findViewById(R.id.btn_add_post);

        addPost.setOnClickListener(v -> {
            showAddPostDialog();
        });
//
//        postList.add(new PostResponse(120, currentUser, "6 minutes ago", new List<Comment>(){}, "Gus", "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//        postList.add(new PostResponse("The Art of Minimalism: Simplifying Your Life"));
//        postList.add(new PostResponse("The Future of Technology: Innovations to Watch"));
//        postList.add(new PostResponse("Mindfulness and Meditation: Finding Peace in a Busy World"));
//        postList.add(new PostResponse("The History of Art: From Cave Paintings to Modern Masterpieces"));
//        postList.add(new PostResponse("Sustainable Living: Tips for a Greener Home"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts);

        recyclerView.setAdapter(new PostAdapter(postList));

        ForumService forumService = RetrofitClient.getClient().create(ForumService.class);
        Call<List<PostResponse>> call = forumService.getPosts();
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful()) {
                    List<PostResponse> posts = response.body();
                    recyclerView.setAdapter(new PostAdapter(posts));
                } else {
                    try {
                        Log.d("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable throwable) {
                Log.d("ERROR", throwable.getMessage());
            }
        });

        recyclerView.setAdapter(new PostAdapter(postList));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void showAddPostDialog() {
        // TODO: FAzer um dialog bonitinho
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_post);

        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);

        EditText content = dialog.findViewById(R.id.editText_content);
        TextView btnConfirm = dialog.findViewById(R.id.button_add_post);



        btnConfirm.setOnClickListener(v -> {

            // Create a post
            ForumService forumService = RetrofitClient.getClientMongo().create(ForumService.class);
            Call<Post> call = forumService.createPost(new Post(currentUser.getId(), content.getText().toString()));
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(getContext(), "Post criado com sucesso", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        try {
                            Log.d("ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable throwable) {
                    Log.d("ERROR", throwable.getMessage());;
                }
            });

        });

        dialog.show();
    }
}
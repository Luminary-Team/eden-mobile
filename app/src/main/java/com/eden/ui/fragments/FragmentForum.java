package com.eden.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForum extends Fragment {

    List<Post> postList = new ArrayList<>();

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

        postList.add(new Post("Exploring the Cosmos: A Journey Through Space and Time"));
        postList.add(new Post("The Art of Minimalism: Simplifying Your Life"));
        postList.add(new Post("The Future of Technology: Innovations to Watch"));
        postList.add(new Post("Mindfulness and Meditation: Finding Peace in a Busy World"));
        postList.add(new Post("The History of Art: From Cave Paintings to Modern Masterpieces"));
        postList.add(new Post("Sustainable Living: Tips for a Greener Home"));
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_posts);

        recyclerView.setAdapter(new PostAdapter(postList));

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

//        recyclerView.setAdapter(new PostAdapter(postList));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void showAddPostDialog() {
        // TODO: FAzer um dialog bonitinho
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Adicionar Novo Post");

        // Configurar o EditText para entrada de texto
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String postContent = input.getText().toString();
                if (!postContent.isEmpty()) {
                    // Adicionar o novo post à lista
                    postList.add(new Post(postContent));
                    // Atualizar o RecyclerView
                    ForumService forumService = RetrofitClient.getClientMongo().create(ForumService.class);
                    Call<Post> call = forumService.createPost(new Post(postContent));
                    call.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {

                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable throwable) {

                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
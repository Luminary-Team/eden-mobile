package com.eden.ui.fragments;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.downloadProfilePicFromFirebase;
import static com.eden.utils.AndroidUtil.isImageFromCamera;
import static com.eden.utils.AndroidUtil.uploadImageToFirebase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.R;
import com.eden.adapter.PostAdapter;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.PostResponse;
import com.eden.api.services.ForumService;
import com.eden.model.Post;
import com.eden.model.PostRequest;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentForum extends Fragment {

    ImageView imagePost;

    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;
    static RecyclerView recyclerView;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri finalSelectedImageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        FloatingActionButton addPost = view.findViewById(R.id.btn_add_post);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_forum);
        progressBar = view.findViewById(R.id.forum_progressBar);

        addPost.setOnClickListener(v -> showAddPostDialog());

        recyclerView = view.findViewById(R.id.recyclerView_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPosts();

        // Reload posts on refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recyclerView.setAdapter(null);
            loadPosts();
            swipeRefreshLayout.setRefreshing(false);
        });

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK &&
                            data != null && data.getData() != null) {

                        Uri selectedImageUri = data.getData();
                        imagePost.setImageURI(selectedImageUri);

                        imagePost.setVisibility(View.VISIBLE);



                        finalSelectedImageUri = selectedImageUri;

//                        if (isImageFromCamera(selectedImageUri)) {
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "perfil_" + System.currentTimeMillis());
//                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");
//
//                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//                            if (uri != null) {
//                                try {
//                                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
//
//                                    byte[] buffer = new byte[1024];
//                                    int bytesRead;
//                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                                        outputStream.write(buffer, 0, bytesRead);
//                                    }
//
//                                    inputStream.close();
//                                    outputStream.close();
//
//                                    Toast.makeText(getContext(), "Foto salva na galeria!", Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(getContext(), "Falha ao salvar a foto!", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(getContext(), "Falha ao inserir no MediaStore!", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "A imagem não foi tirada com a câmera, não será salva.", Toast.LENGTH_SHORT).show();
//                        }

                        // Exibe a imagem no perfil
                        Glide.with(this).load(selectedImageUri).into(imagePost);
                    }
                }
        );

        return view;
    }

    public static void loadPosts() {
        // Get all posts
        progressBar.setVisibility(View.VISIBLE);
        ForumService forumService = RetrofitClient.getClient().create(ForumService.class);
        Call<List<PostResponse>> call = forumService.getPosts();
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    List<PostResponse> posts = response.body();
                    if (posts != null) {
                        recyclerView.setAdapter(new PostAdapter(posts));
                    } else {
                        // Fazer imagenzinha de vazio
                    }
                } else {
                    try {
                        progressBar.setVisibility(View.GONE);
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

    }

    private void showAddPostDialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_post);

        dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ImageButton back_btn = dialog.findViewById(R.id.back_btn);
        EditText content = dialog.findViewById(R.id.editText_content);
        TextView btnConfirm = dialog.findViewById(R.id.button_add_post);
        ImageView buttonImage = dialog.findViewById(R.id.button_add_image);
        ShapeableImageView pfp = dialog.findViewById(R.id.comment_pfp);
        imagePost = dialog.findViewById(R.id.image_post);

        imagePost.setVisibility(View.GONE);

        downloadProfilePicFromFirebase(getContext(), pfp);

        buttonImage.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

        btnConfirm.setOnClickListener(v -> {

            // Create a post
            ForumService forumService = RetrofitClient.getClientMongo().create(ForumService.class);
            Call<PostResponse> call = forumService.createPost(new PostRequest(currentUser.getId(), content.getText().toString()));

            call.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if(response.isSuccessful()) {
                        PostResponse post = response.body();
                        Toast.makeText(getContext(), "Post criado com sucesso", Toast.LENGTH_SHORT).show();
                        if (finalSelectedImageUri != null)
                            uploadImageToFirebase(finalSelectedImageUri, "post_" + post.getId() + ".jpg");
                        loadPosts();
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
                public void onFailure(Call<PostResponse> call, Throwable throwable) {
                    Log.d("ERROR", throwable.getMessage());;
                }
            });

        });

        back_btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}
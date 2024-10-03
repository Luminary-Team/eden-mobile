package com.eden;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.model.User;
import com.eden.utils.AndroidUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic;
    Button btnSave;

    User userEdited = new User();

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_edit);

        // Selecionando imagem
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);
                        }
                    }
                }
        );

        profilePic = findViewById(R.id.profile_pic);
        btnSave = findViewById(R.id.btn_save_profile);

        AndroidUtil.downloadImageFromFirebase(this, "ProfilePic" + FirebaseAuth.getInstance().getUid(), profilePic);

        // Adicionando foto de perfil
        profilePic.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profilePic.setImageURI(selectedImageUri);

            btnSave.setOnClickListener(v -> {
                AndroidUtil.uploadImageToFirebase(selectedImageUri, "ProfilePic_" + FirebaseAuth.getInstance().getUid());
                Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
            });
        }
    }



}
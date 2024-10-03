package com.eden;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.model.User;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic;
    Button btnSave;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_edit);

        // TODO: Criar um User() com os valores recebidos

        // Selecionando imagem
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if(result.getResultCode() == Activity.RESULT_OK &&
                            data!=null && data.getData()!=null) {

                        Uri selectedImageUri = data.getData();
                        profilePic.setImageURI(selectedImageUri);

                        Uri finalSelectedImageUri = selectedImageUri;
                        btnSave.setOnClickListener(v -> {
                            AndroidUtil.uploadImageToFirebase(finalSelectedImageUri);
                            Log.d("LOG", "onActivityResult: deu bom");
                            Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
                        });

                        selectedImageUri = data.getData();
                        Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);
                    }
                }
        );

        profilePic = findViewById(R.id.profile_pic);
        btnSave = findViewById(R.id.btn_save_profile);

        AndroidUtil.downloadImageFromFirebase(this, profilePic);

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

        // Logout
        (findViewById(R.id.textView_logout)).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserProfileEdit.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }



}
package com.eden;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic = findViewById(R.id.profile_pic);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_edit);

        // Adicionando foto de perfil
        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profilePic.setImageURI(selectedImageUri);
        }
    }

//    private ActivityResultLauncher<Intent> resultLauncherGaleria = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                Uri imageUri = result.getData().getData();
//                if (imageUri != null) {
////                    foto.setVisibility(View.VISIBLE);
////                    foto.setImageURI(imageUri);
//                }
//            });
}
package com.eden;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.eden.utils.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_edit);

        profilePic = findViewById(R.id.profile_pic);
        btnSave = findViewById(R.id.btn_save_profile);

        AndroidUtil.downloadImageFromFirebase(this, "ProfilePic" + FirebaseAuth.getInstance().getUid(), profilePic);

        // Adicionando foto de perfil
        profilePic.setOnClickListener(v -> {
            Log.d("CHECKPOINT", "Clicou no profile");
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

            btnSave.setOnClickListener(v -> {
                AndroidUtil.uploadImageToFirebase(selectedImageUri, "ProfilePic_" + FirebaseAuth.getInstance().getUid());
                Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
            });
        }
    }



}
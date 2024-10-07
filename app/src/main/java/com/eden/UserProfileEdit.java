package com.eden;

import static com.eden.utils.AndroidUtil.getUser;

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
import com.eden.api.RetrofitClient;
import com.eden.api.dto.UserSchema;
import com.eden.api.services.UserService;
import com.eden.model.User;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        btnSave = findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(v -> {

            String updatedName = ((TextView) findViewById(R.id.editText_full_name)).getText().toString();
            String updatedUserName = ((TextView) findViewById(R.id.editText_username)).getText().toString();
            String updatedPhone = ((TextView) findViewById(R.id.editText_phone)).getText().toString();

            // Setting new values to user
            User updatedUser = new User();
            updatedUser.setName(updatedName);
            updatedUser.setUserName(updatedUserName);
            updatedUser.setCellphone(updatedPhone);

            // Calling api (CALL ME MAYBE)
            editUser(updatedUser);
        });

        // Selecionando imagem
//        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    Intent data = result.getData();
//                    if(result.getResultCode() == Activity.RESULT_OK &&
//                            data!=null && data.getData()!=null) {
//
//                        Uri selectedImageUri = data.getData();
//                        profilePic.setImageURI(selectedImageUri);
//
//                        Uri finalSelectedImageUri = selectedImageUri;
//
//                        // Save user information button
//                        btnSave.setOnClickListener(v -> {
//
//
//
//                            AndroidUtil.uploadImageToFirebase(finalSelectedImageUri);
//                            Log.d("LOG", "onActivityResult: deu bom");
//                            Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
//                        });
//
//                        selectedImageUri = data.getData();
//                        Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);
//                    }
//                }
//        );

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

    public void editUser(User newUser) {
        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);

        UserSchema user = getUser();

        Call<UserSchema> call = userService.updateUser(newUser, user.getId());

        call.enqueue(new Callback<UserSchema>() {
            @Override
            public void onResponse(Call<UserSchema> call, Response<UserSchema> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfileEdit.this, "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileEdit.this, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserSchema> call, Throwable throwable) {
                Log.d("CHECKPOINT", throwable.getMessage());
            }
        });
    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
}
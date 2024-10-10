package com.eden;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.getUser;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.camera.core.ImageCapture;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic;
    Button btnSave;
    boolean isProfilePicSelected = false;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri finalSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_edit);

        // Setting Values
        TextView editName = findViewById(R.id.editText_full_name);
        TextView editUserName = findViewById(R.id.editText_username);
        TextView editPhone = findViewById(R.id.editText_phone);

        profilePic = findViewById(R.id.profile_pic);
        btnSave = findViewById(R.id.btn_save_profile);

        if (currentUser != null) {
            editName.setText(currentUser.getName());
            editUserName.setText(currentUser.getUserName());
            editPhone.setText(currentUser.getCellphone());
        }

        btnSave = findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(v -> {

            String updatedName = editName.getText().toString();
            String updatedUserName = editUserName.getText().toString();
            String updatedPhone = editPhone.getText().toString();

            // Setting new values to user
            UserSchema updatedUser = new UserSchema();
            updatedUser.setName(updatedName);
            updatedUser.setUserName(updatedUserName);
            updatedUser.setCellphone(updatedPhone);

            // Verifies if profile picture was selected
            if (isProfilePicSelected) {
                AndroidUtil.uploadProfilePicToFirebase(finalSelectedImageUri);
                Log.d("LOG", "onActivityResult: deu bom");
                Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
                isProfilePicSelected = false;
            }

            // Calling api (CALL ME MAYBE)
            saveUser(updatedUser);
        });

//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }

        // Choosing perfil photo
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

        // Changing profile picture and uploading to firebase
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // TODO : Salvar na galeria

                    Intent data = result.getData();
                    if(result.getResultCode() == Activity.RESULT_OK &&
                            data!=null && data.getData()!=null) {

                        Uri selectedImageUri = data.getData();
                        profilePic.setImageURI(selectedImageUri);

                        finalSelectedImageUri = selectedImageUri;

                        // Defines user has selected an image
                        isProfilePicSelected = true;


                        // Saves image to gallery if user has taken a photo
                        if (data.getExtras() != null && data.getExtras().containsKey("android.intent.extras.CAMERA_IMAGE")) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "perfil_" + System.currentTimeMillis());
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");

                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                            try {
                                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                                OutputStream outputStream = getContentResolver().openOutputStream(uri);

                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }

                                inputStream.close();
                                outputStream.close();

                                Toast.makeText(UserProfileEdit.this, "Foto salva na galeria!", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        selectedImageUri = data.getData();
                        Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);
                    }
                }
        );

        AndroidUtil.downloadProfilePicFromFirebase(this, profilePic);

        // Logout
        (findViewById(R.id.textView_logout)).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserProfileEdit.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }

    public void saveUser(UserSchema newUser) {

        // Updating user
        UserService userService = RetrofitClient.getClientWithToken().create(UserService.class);

        Call<UserSchema> call = userService.updateUser(newUser, String.valueOf(currentUser.getId()));

        call.enqueue(new Callback<UserSchema>() {
            @Override
            public void onResponse(Call<UserSchema> call, Response<UserSchema> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfileEdit.this, "Usuário atualizado!", Toast.LENGTH_SHORT).show();
                    Log.d("USER", "Deu bom");
                }
            }

            @Override
            public void onFailure(Call<UserSchema> call, Throwable throwable) {

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
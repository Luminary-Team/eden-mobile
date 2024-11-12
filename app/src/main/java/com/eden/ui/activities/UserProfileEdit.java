package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.favorites;
import static com.eden.utils.AndroidUtil.isImageFromCamera;
import static com.eden.utils.AndroidUtil.openActivity;
import static com.eden.utils.AndroidUtil.token;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.UserEditRequest;
import com.eden.api.dto.UserSchema;
import com.eden.api.services.UserService;
import com.eden.utils.AndroidUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileEdit extends AppCompatActivity {

    ImageView profilePic;
    Button btnSave;
    boolean isProfilePicSelected = false;
    private String unformattedPhoneNumber;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri finalSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        // Setting Values
        EditText editName = findViewById(R.id.editText_full_name);
        EditText editUserName = findViewById(R.id.editText_username);
        EditText editPhone = findViewById(R.id.editText_phone);

        profilePic = findViewById(R.id.profile_pic);
        btnSave = findViewById(R.id.btn_save_profile);

        if (currentUser != null) {
            editName.setText(currentUser.getName());
            editUserName.setText(currentUser.getUserName());
            editPhone.setText(formatPhone(currentUser.getCellphone()));
        }

        formatPhone(editPhone);

        btnSave = findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(v -> {

            String updatedName = editName.getText().toString();
            String updatedUserName = editUserName.getText().toString();

            // Setting new values to user
            UserEditRequest updatedUser = new UserEditRequest();
            updatedUser.setName(updatedName);
            updatedUser.setUserName(updatedUserName);
            updatedUser.setCellphone(unformattedPhoneNumber);
            updatedUser.setCpf(currentUser.getCpf());

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

                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK &&
                            data != null && data.getData() != null) {

                        Uri selectedImageUri = data.getData();
                        profilePic.setImageURI(selectedImageUri);

                        finalSelectedImageUri = selectedImageUri;

                        // Define que o usuário selecionou uma imagem
                        isProfilePicSelected = true;

                        // Verifica se a imagem foi tirada com a câmera
                        if (isImageFromCamera(selectedImageUri)) {
                            // Tentar salvar a imagem na galeria
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "perfil_" + System.currentTimeMillis());
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");

                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                            if (uri != null) {
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
                                    Toast.makeText(UserProfileEdit.this, "Falha ao salvar a foto!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserProfileEdit.this, "Falha ao inserir no MediaStore!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserProfileEdit.this, "A imagem não foi tirada com a câmera, não será salva.", Toast.LENGTH_SHORT).show();
                        }

                        // Exibe a imagem no perfil
                        Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(profilePic);
                    }
                }
        );

        AndroidUtil.downloadProfilePicFromFirebase(this, profilePic);

        (findViewById(R.id.back_btn)).setOnClickListener(v -> {
            finish();
        });

        // Logout
        (findViewById(R.id.textView_logout)).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            currentUser = null;
            favorites = null;
            token = null;
            Intent intent = new Intent(UserProfileEdit.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    private void saveUser(UserEditRequest newUser) {

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
                // TODO: Tratar erros
            }
        });
    }

    private String formatPhone(String phoneNumber) {
        StringBuilder formatted = new StringBuilder();
        String unformatted = phoneNumber.replaceAll("[^\\d]", ""); // Remove tudo que não é dígito

        if (unformatted.length() > 0) {
            formatted.append("(");
            formatted.append(unformatted.substring(0, Math.min(unformatted.length(), 2))); // DDD
            if (unformatted.length() >= 3) {
                formatted.append(") ");
                formatted.append(unformatted.substring(2, Math.min(unformatted.length(), 7))); // Primeira parte do número
                if (unformatted.length() >= 8) {
                    formatted.append("-");
                    formatted.append(unformatted.substring(7)); // Segunda parte do número
                }
            }
        }
        return formatted.toString();
    }

    private void formatPhone(EditText editPhone) {

        // Format phoneNumber
        editPhone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) { return; }

                isUpdating = true;
                unformattedPhoneNumber = s.toString().replaceAll("[^\\d]", ""); // Armazena o número não formatado

                if (unformattedPhoneNumber.length() > 11) {
                    unformattedPhoneNumber = unformattedPhoneNumber.substring(0, 11); // Limita a 11 dígitos
                }

                StringBuilder formatted = new StringBuilder();
                int length = unformattedPhoneNumber.length();

                if (length > 0) {
                    formatted.append("(");
                    formatted.append(unformattedPhoneNumber.substring(0, Math.min(length, 2))); // DDD
                    if (length >= 3) {
                        formatted.append(") ");
                        formatted.append(unformattedPhoneNumber.substring(2, Math.min(length, 7))); // Primeira parte do número
                        if (length >= 8) {
                            formatted.append("-");
                            formatted.append(unformattedPhoneNumber.substring(7)); // Segunda parte do número
                        }
                    }
                }
                editPhone.setText(formatted.toString());
                int selectionPosition = formatted.length();

                if (selectionPosition > editPhone.getText().length()) {
                    selectionPosition = editPhone.getText().length();
                }
                editPhone.setSelection(selectionPosition); // Define a posição da seleção corretamente
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
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
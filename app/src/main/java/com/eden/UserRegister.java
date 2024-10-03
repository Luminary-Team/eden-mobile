package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.api.UserApi;
import com.eden.model.User;
import com.eden.utils.FirebaseUserUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRegister extends AppCompatActivity {

    FirebaseUserUtil db = new FirebaseUserUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        EditText nameEditText = findViewById(R.id.textInput_nome);
        EditText phoneNumberEditText = findViewById(R.id.textInput_numero);
        EditText cpfEditText = findViewById(R.id.textInput_cpf);
        EditText emailEditText = findViewById(R.id.textInput_email);
        EditText passwordEditText = findViewById(R.id.textInput_senha);
        TextView btnRegister = findViewById(R.id.btn_cadastro);

        // Register the user
        btnRegister.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String cpf = cpfEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Verifies if none of the values are null
            if (!name.isEmpty() && !phoneNumber.isEmpty()
                    && !cpf.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                registerUser (name, cpf, phoneNumber, email, password);
            } else {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }
        });

        // In case the user has an account, redirect to login
        (findViewById(R.id.textView_login)).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
            finish();
        });


    }

    // Saves user on database
    public void registerUser(String name, String cpf, String phoneNumber, String email, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://desenvolvimento-ii.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);

        Log.d("CHECKPOINT", phoneNumber);

        Call<ResponseBody> userCall = api.userRegister(new User(cpf, name, name,
                phoneNumber, 0, email, password));

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Salvar o user no firebaseAuth
                        db.register(email, password, UserRegister.this);

                        Log.d("CHECKPOINT", "JSON Object: " + jsonObject.toString());
                        Log.d("CHECKPOINT", response.message());

                    } else if (response.errorBody() != null) {
                        // Tratando erros
                        String errorResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorResponse);

                        // Verifica os campos que contêm erros e define mensagens de erro nos EditTexts
                        if (jsonObject.has("cpf")) {
                            String cpfError = jsonObject.getString("cpf");
                            EditText cpfEditText = findViewById(R.id.textInput_cpf);
                            cpfEditText.setError(cpfError);
                        }

                        if (jsonObject.has("cellphone")) {
                            String phoneError = jsonObject.getString("phoneNumber");
                            EditText phoneEditText = findViewById(R.id.textInput_numero);
                            phoneEditText.setError(phoneError);
                        }

                        if (jsonObject.has("email")) {
                            String emailError = jsonObject.getString("email");
                            EditText emailEditText = findViewById(R.id.textInput_email);
                            emailEditText.setError(emailError);
                        }

                        if (jsonObject.has("password")) {
                            String passwordError = jsonObject.getString("password");
                            EditText passwordEditText = findViewById(R.id.textInput_senha);
                            passwordEditText.setError(passwordError);
                        }

                        // Exibe os erros no log
                        Log.d("CHECKPOINT", "Erros recebidos: " + jsonObject.toString());
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.d("CHECKPOINT", throwable.getMessage());
            }
        });
    }

    public void getToken(String email, String password) {

    }

}
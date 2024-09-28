package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.api.UserApi;
import com.eden.model.User;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;

import java.util.Collections;
import java.util.List;

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
        setContentView(R.layout.activity_user_cadastro);

        EditText name = findViewById(R.id.textInput_nome);
        EditText phoneNumber = findViewById(R.id.textInput_numero);
        EditText email = findViewById(R.id.textInput_email);
        EditText password = findViewById(R.id.textInput_senha);
        TextView btnRegister = findViewById(R.id.btn_cadastro);

        //
        btnRegister.setOnClickListener(v -> {
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                    || phoneNumber.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }
            saveUser(email.getText().toString(), password.getText().toString());
            db.register(email.getText().toString(), password.getText().toString(), this);
        });

    }

    // Saves user on the
    public void saveUser(String email, String password) {
        Retrofit retrofit = UserApi.retrofit;

        UserApi api = retrofit.create(UserApi.class);
        Call<User> userCall = api.userRegister(email, password);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });
    }

}
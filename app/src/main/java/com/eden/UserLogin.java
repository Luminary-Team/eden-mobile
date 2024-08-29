package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.utils.FirebaseUserUtil;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    FirebaseUserUtil db = new FirebaseUserUtil();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        EditText email = findViewById(R.id.textInput_email);
        EditText senha = findViewById(R.id.textInput_senha);
        TextView btnLogin = findViewById(R.id.btn_login);
        TextView btnCadastro = findViewById(R.id.textView_cadastrar);

        btnLogin.setOnClickListener(v -> {
            if (email.getText().toString().equals("") || senha.getText().toString().equals("")) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }

            db.login(email.getText().toString(), senha.getText().toString(), this);
        });

        btnCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserCadastro.class);
            startActivity(intent);
            finish();
        });

    }
}
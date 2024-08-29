package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.utils.FirebaseUserUtil;

public class UserCadastro extends AppCompatActivity {

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

        btnRegister.setOnClickListener(v -> {
            if (email.getText().toString().equals("") || password.getText().toString().equals("")
                    || phoneNumber.getText().toString().equals("") || name.getText().toString().equals("")) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }
            db.register(email.getText().toString(), password.getText().toString(), this);
        });

    }
}
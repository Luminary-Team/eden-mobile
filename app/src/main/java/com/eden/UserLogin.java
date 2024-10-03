package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.utils.FirebaseUserUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    FirebaseUserUtil db = new FirebaseUserUtil();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        EditText email = findViewById(R.id.textInput_email_login);
        EditText senha = findViewById(R.id.textInput_senha_login);
        TextView btnLogin = findViewById(R.id.btn_login);
        ImageView passwordToggle = findViewById(R.id.passwordToggle);

        btnLogin.setOnClickListener(v -> {
            if (email.getText().toString().equals("") || senha.getText().toString().equals("")) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }

            db.login(email.getText().toString(), senha.getText().toString(), this);
        });

        // In case the user doesn't have an account, redirect to UserRegister
        (findViewById(R.id.textView_cadastro)).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserRegister.class);
            startActivity(intent);
            finish();
        });

        // Mostrar e ocultar senha

        final boolean[] isPasswordVisible = {false};

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible[0]) {
                    // Se a senha está visível, ocultar
                    senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_off_icon);  // Ícone de olho fechado
                } else {
                    // Se a senha está oculta, mostrar
                    senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_on_icon);  // Ícone de olho aberto
                }
                isPasswordVisible[0] = !isPasswordVisible[0];  // Inverter estado

                // Mover o cursor para o final do texto após alterar a visibilidade
                senha.setSelection(senha.getText().length());
            }
        });

    }
}
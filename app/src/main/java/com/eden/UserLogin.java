package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.utils.AndroidUtil;
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
        EditText password = findViewById(R.id.textInput_senha_login);
        TextView errorMessage = findViewById(R.id.error_message);
        TextView btnLogin = findViewById(R.id.btn_login);
        ImageView passwordToggle = findViewById(R.id.passwordToggle);
        TextView btnCadastro = findViewById(R.id.textView_cadastro);

        // Setting underline text
        btnCadastro.setPaintFlags(btnCadastro.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnLogin.setOnClickListener(v -> db.login(email, password, errorMessage, this));

        // In case the user doesn't have an account, redirect to UserRegister
        btnCadastro.setOnClickListener(v -> {
            AndroidUtil.openActivity(this, UserRegister.class);
            finish();
        });

        // Show and hide password
        final boolean[] isPasswordVisible = {false};

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible[0]) {
                    // Se a senha está visível, ocultar
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_off_icon);  // Ícone de olho fechado
                } else {
                    // Se a senha está oculta, mostrar
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_on_icon);  // Ícone de olho aberto
                }
                isPasswordVisible[0] = !isPasswordVisible[0];  // Inverter estado

                // Mover o cursor para o final do texto após alterar a visibilidade
                password.setSelection(password.getText().length());
            }
        });

    }

}
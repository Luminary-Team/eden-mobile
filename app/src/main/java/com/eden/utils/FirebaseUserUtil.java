package com.eden.utils;

import static com.eden.utils.AndroidUtil.authenticate;
import static com.eden.utils.AndroidUtil.openActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.eden.ui.activities.MainActivity;
import com.eden.R;
import com.eden.ui.activities.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserUtil {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static String currentUserId(){ return FirebaseAuth.getInstance().getUid(); }

    public static boolean isLoggedIn(){
        return currentUserId() != null;
    }

    public void login(EditText emailEditText, EditText passwordEditText, TextView errorMessage, Context context) {
        if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            // TODO: Aplicar tratamento de erros
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                                authenticate(context);
                            } else {
                                // Treatment for the email text field
                                emailEditText.setBackgroundResource(R.drawable.rounded_corner_shape_error);
                                passwordEditText.setBackgroundResource(R.drawable.rounded_corner_shape_error);

                                emailEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        errorMessage.setVisibility(View.INVISIBLE);
                                        emailEditText.setError(null);
                                        emailEditText.setBackgroundResource(R.drawable.rounded_corner_shape);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                    }

                                });
                                passwordEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        errorMessage.setVisibility(View.INVISIBLE);
                                        passwordEditText.setError(null);
                                        passwordEditText.setBackgroundResource(R.drawable.rounded_corner_shape);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                    }

                                });

                            }
                        }
                    }).addOnFailureListener(e -> {
                        errorMessage.setTextColor(ContextCompat.getColor(context, R.color.errorRed));
                        errorMessage.setVisibility(View.VISIBLE);
                        Log.d("login", "Login falhou: " + e.getMessage());
                        if (e.getMessage().contains("The email address is badly formatted.")) {
                            errorMessage.setText("Email inválido");
                        } else {
                            errorMessage.setText("Email ou senha incorretos");
                        }
                    });
        } else {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setTextColor(ContextCompat.getColor(context, R.color.edenBlue));
            errorMessage.setText("Preencha todos os campos");
        }
    }

    public void register(String email, String senha, Context context) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Ir para home
                            authenticate(context);

                            Log.w("register", "onComplete: " + user.getUid());
                        } else {
                            Log.e("register", "Registration failed: " + task.getException().getMessage());
                        }
                    }
        });
    }

}

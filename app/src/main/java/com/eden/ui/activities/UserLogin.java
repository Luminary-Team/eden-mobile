package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.R;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    FirebaseUserUtil db = new FirebaseUserUtil();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText email;
    ProgressBar progressBar;
    EditText password;
    TextView errorMessage;
    Button btnLogin;
    ImageView passwordToggle;
    TextView btnCadastro;
    TextView btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        email = findViewById(R.id.textInput_email_login);
        password = findViewById(R.id.textInput_senha_login);
        errorMessage = findViewById(R.id.error_message);
        btnLogin = findViewById(R.id.btn_login);
        passwordToggle = findViewById(R.id.login_password_toggle);
        btnCadastro = findViewById(R.id.textView_cadastro);
        btnForgotPassword = findViewById(R.id.textView_forgot_password);
        progressBar = findViewById(R.id.user_register_progressBar);

        progressBar.setVisibility(View.GONE);

        // Style of the text
        String fullText = "Não tem uma conta? Cadastre-se";
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf("Cadastre-se");
        int end = start + "Cadastre-se".length();
        spannableString.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.edenVeryLightBlue)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        btnCadastro.setText(spannableString);

        // Login
        btnLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setText("");
            btnLogin.setEnabled(false);
            login(email, password, errorMessage, progressBar, this);
        });

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

        btnForgotPassword.setOnClickListener(v -> {
            AndroidUtil.openActivity(this, ChangePassword.class);
        });

    }


    public void login(EditText emailEditText, EditText passwordEditText, TextView errorMessage, ProgressBar progressBar, Context context) {
        if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setText("");
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
                                progressBar.setVisibility(View.GONE);
                                btnLogin.setText("Login");
                                btnLogin.setEnabled(true);
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
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnLogin.setText("Login");
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setTextColor(ContextCompat.getColor(context, R.color.edenBlue));
            errorMessage.setText("Preencha todos os campos");
        }
    }

}
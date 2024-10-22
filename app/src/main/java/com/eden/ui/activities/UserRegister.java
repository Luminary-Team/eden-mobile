package com.eden.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.services.UserService;
import com.eden.model.User;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegister extends AppCompatActivity {

    FirebaseUserUtil db = new FirebaseUserUtil();
    private String unformattedPhoneNumber, unformattedCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        EditText nameEditText = findViewById(R.id.textInput_nome);
        EditText phoneNumberEditText = findViewById(R.id.textInput_numero);
        EditText cpfEditText = findViewById(R.id.textInput_cpf);
        EditText emailEditText = findViewById(R.id.textInput_email);
        EditText passwordEditText = findViewById(R.id.textInput_senha);
        ImageView passwordToggle = findViewById(R.id.register_password_toggle);
        Button btnRegister = findViewById(R.id.btn_cadastro);
        TextView btnLogin = findViewById(R.id.textView_login);

        // Setting underline text
        btnLogin.setPaintFlags(btnLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Register the user
        btnRegister.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Verifies if none of the values are null
            if (!name.isEmpty() && !unformattedPhoneNumber.isEmpty()
                    && !unformattedCpf.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                registerUser(name, unformattedCpf, unformattedPhoneNumber, email, password);
            } else {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            }
        });

        // In case the user has an account, redirect to login
        btnLogin.setOnClickListener(v -> {
            AndroidUtil.openActivity(this, UserLogin.class);
            finish();
        });


        // Mostrar e ocultar senha

        final boolean[] isPasswordVisible = {false};

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible[0]) {
                    // Se a senha está visível, ocultar
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_off_icon);  // Ícone de olho fechado
                } else {
                    // Se a senha está oculta, mostrar
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.eye_on_icon);  // Ícone de olho aberto
                }
                isPasswordVisible[0] = !isPasswordVisible[0];  // Inverter estado

                // Mover o cursor para o final do texto após alterar a visibilidade
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        // Formatar Cellphone
        formatPhone(phoneNumberEditText);

        // Formatar CPF
        formatCpf(cpfEditText);

    }

    // Saves user on database
    public void registerUser(String name, String cpf, String phoneNumber, String email, String password) {

        UserService api = RetrofitClient.getClient().create(UserService.class);

        // Creating User
        User user = new User(cpf, name, name,
                password, 0, email, phoneNumber);
        Call<ResponseBody> userCall = api.userRegister(user);

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Salvar o user no firebaseAuth
                        db.register(email, password, UserRegister.this);

                        // Ir para home
                        AndroidUtil.openActivity(UserRegister.this, MainActivity.class);

                        Log.d("CHECKPOINT", "JSON Object: " + jsonObject.toString());
                        Log.d("CHECKPOINT", response.message());

                    } else if (response.errorBody() != null) {
                        // Tratando erros
                        String errorResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorResponse);

                        if (jsonObject.has("message")) {
                            String messageError = jsonObject.getString("message");
                            if (messageError.trim().toLowerCase().contains("cpf")) {
                                EditText cpfEditText = findViewById(R.id.textInput_cpf);
                                cpfEditText.setError(messageError);
                            }
                        }

                        // Verifica os campos que contêm erros e define mensagens de erro nos EditTexts
                        if (jsonObject.has("cpf")) {
                            String cpfError = jsonObject.getString("cpf");
                            EditText cpfEditText = findViewById(R.id.textInput_cpf);
                            cpfEditText.setError(cpfError);
                        }

                        if (jsonObject.has("cellphone")) {
                            String phoneError = jsonObject.getString("cellphone");
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

    private void formatCpf(EditText cpfEditText) {
        cpfEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String cpf = s.toString();

                // Armazena o CPF não formatado
                unformattedCpf = cpf.replaceAll("[^0-9]", "");

                cpf = cpf.replaceAll("[^0-9]", "");

                if (!cpf.isEmpty()) {
                    if (cpf.length() >= 3) {
                        if (cpf.length() <= 6) {
                            cpf = cpf.substring(0, 3) + "." + cpf.substring(3);
                        } else if (cpf.length() <= 9) {
                            cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6);
                        } else if (cpf.length() <= 11) {
                            cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
                        }
                    }

                    if (!cpf.equals(s.toString())) {
                        cpfEditText.removeTextChangedListener(this);
                        cpfEditText.setText(cpf);
                        cpfEditText.setSelection(cpf.length());
                        cpfEditText.addTextChangedListener(this);
                    }
                }
            }
        });
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


}
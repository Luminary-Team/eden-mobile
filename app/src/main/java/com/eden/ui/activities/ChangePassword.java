package com.eden.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eden.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.recover_emailEditText);
        Button btnSend = findViewById(R.id.sendEmailButton);

        btnSend.setOnClickListener(v -> {
            if (!email.getText().toString().isEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Email enviado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}
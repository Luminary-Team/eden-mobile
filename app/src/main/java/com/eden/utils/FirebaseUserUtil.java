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
import android.widget.ProgressBar;
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

package com.eden.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eden.MainActivity;
import com.eden.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseUserUtil {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static String currentUserId(){ return FirebaseAuth.getInstance().getUid(); }

    public static boolean isLoggedIn(){
        return currentUserId() != null;
    }

    public void login(String email, String senha, Context context) {
        // TODO: Aplicar tratamento de erros
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(context, intent, null);
                        } else {
                            // Caso de falha
                            Toast.makeText(context, "Falha no login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void register(String email, String senha, Context context) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Usuário cadastrado no firebase com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Caso de falha
                            Toast.makeText(context, "Falha ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
        });
    }

}

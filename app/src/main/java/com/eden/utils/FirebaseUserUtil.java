package com.eden.utils;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseUserUtil {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void login(String email, String senha, Context context) {
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .whereEqualTo("senha", senha)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                Intent intent = new Intent(context, MainActivity.class);
//                                intent.putExtra("user", user);
                                context.startActivity(intent);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erro ao realizar o login\nErro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void register(String email, String senha, Context context) {
        db.collection("usuarios").document().set(new User(email, senha))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        login(email, senha, context);
                    }
                });
    }
}

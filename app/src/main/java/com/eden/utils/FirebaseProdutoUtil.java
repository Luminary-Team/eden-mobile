package com.eden.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eden.model.Produto;
import com.eden.adapter.ProdutosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseProdutoUtil {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void salvarProduto(Produto produto, Context context) {
        // Add a new document with a generated ID
        db.collection("produtos")
                .document()
                .set(produto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // DocumentSnapshot document = task.getResult();
                        Toast.makeText(context, "Produto inserido com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erro ao inserir o produto\nErro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void  listarProdutos(List<Produto> listaProdutos, ProdutosAdapter adapter) {
        db.collection("produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listaProdutos.add(document.toObject(Produto.class));
                            }
                        }
                        adapter.notifyItemChanged(listaProdutos.size());
                    }
                });
    }
}


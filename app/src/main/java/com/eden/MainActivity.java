package com.eden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eden.adapter.ProdutosAdapter;
import com.eden.model.Produto;
import com.eden.utils.FirebaseProdutoUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BottomNavigationView footer;
    List<Produto> produtos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseProdutoUtil db = new FirebaseProdutoUtil();

        ImageView btnSidebar = findViewById(R.id.btnSidebar);
        ImageView btnCarrinho = findViewById(R.id.btnCarrinho);

        footer = findViewById(R.id.footer_navigation);
        recyclerView = findViewById(R.id.recyclerView);

        // Settando o adapter da RecyclerView
        ProdutosAdapter adapter = new ProdutosAdapter(produtos);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        db.listarProdutos(produtos, adapter);

        btnSidebar.setOnClickListener(v -> {

            Dialog dialog = new Dialog(this);

            // Inflando o layout personalizado
            dialog.setContentView(R.layout.dialog_options);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Recuperando os elementos do diálogo
            TextView optionPerfil = dialog.findViewById(R.id.optionPerfil);
            TextView optionFavoritos = dialog.findViewById(R.id.optionFavoritos);
            TextView optionPontosDeColeta = dialog.findViewById(R.id.optionPontosDeColeta);
            TextView optionArtigosInformativos = dialog.findViewById(R.id.optionArtigosInformativos);
            TextView optionConfiguracoes = dialog.findViewById(R.id.optionConfiguracoes);

            // Definindo comportamentos para cada opção
            optionPerfil.setOnClickListener(view -> {
                // Lógica para o Perfil
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                dialog.dismiss();
                // Adicione aqui o código para abrir o perfil ou outra ação
            });

            optionFavoritos.setOnClickListener(view -> {
                // Lógica para Favoritos
                dialog.dismiss();
                // Adicione aqui o código para abrir favoritos ou outra ação
            });

            optionPontosDeColeta.setOnClickListener(view -> {
                // Lógica para Pontos de Coleta
                Intent intent = new Intent(this, PontoColeta.class);
                startActivity(intent);
                dialog.dismiss();
                // Adicione aqui o código para abrir pontos de coleta ou outra ação
            });

            optionArtigosInformativos.setOnClickListener(view -> {
                // Lógica para Artigos Informativos
                dialog.dismiss();
                // Adicione aqui o código para abrir artigos informativos ou outra ação
            });

            optionConfiguracoes.setOnClickListener(view -> {
                // Lógica para Configurações
                dialog.dismiss();
                // Adicione aqui o código para abrir configurações ou outra ação
            });

            dialog.show();

        });

        btnCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        footer.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_add){
                Intent intent = new Intent(MainActivity.this, RegisterProduct.class);
                startActivity(intent);
            }
            if(item.getItemId() == R.id.menu_home){
                recyclerView.setVisibility(View.VISIBLE);
            }
            if(item.getItemId() == R.id.menu_forum){
                recyclerView.setVisibility(View.GONE);
            }
            return false;
        });

        footer.setSelectedItemId(R.id.menu_home);

    }
}
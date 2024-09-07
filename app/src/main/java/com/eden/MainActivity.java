package com.eden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eden.adapter.ProdutosAdapter;
import com.eden.model.Produto;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseProdutoUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseProdutoUtil db = new FirebaseProdutoUtil();

        List<Produto> produtos = new ArrayList<>();

        // Pegando os elementos do layout
        ImageView btnSidebar = findViewById(R.id.btnSidebar);
        ImageView btnCarrinho = findViewById(R.id.btnCarrinho);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        BottomNavigationView footer = findViewById(R.id.footer_navigation);
        NavigationView navView = findViewById(R.id.nav_view);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Settando o adapter da RecyclerView
        ProdutosAdapter adapter = new ProdutosAdapter(produtos);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // TEMP
        db.listarProdutos(produtos, adapter);

        // Botão do carrinho
        btnCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        // Botão do sidebar
        btnSidebar.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Configurações da Sidebar
        navView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.nav_favoritos)
                Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show();
            if (menuItem.getItemId() == R.id.nav_pontos)
                Toast.makeText(this, "Pontos de Coleta", Toast.LENGTH_SHORT).show();
            if (menuItem.getItemId() == R.id.nav_artigos)
                Toast.makeText(this, "Artigos", Toast.LENGTH_SHORT).show();
            if (menuItem.getItemId() == R.id.nav_favoritos)
                AndroidUtil.openActivity(this, UserProfile.class);

            // Fechar o drawer após a seleção
            drawerLayout.closeDrawers();

            return true;
        });

        // Configurações do Navigation Footer
        footer.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_add){
                Intent intent = new Intent(MainActivity.this, RegisterProduct.class);
                startActivity(intent);
            }
            if(item.getItemId() == R.id.menu_home){
                recyclerView.setVisibility(View.VISIBLE);
            }
            if(item.getItemId() == R.id.menu_forum){

            }
            return false;
        });

        footer.setSelectedItemId(R.id.menu_home);

    }
}
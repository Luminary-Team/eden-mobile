package com.eden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eden.utils.AndroidUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pegando os elementos do layout
        ImageView btnSidebar = findViewById(R.id.btnSidebar);
        ImageView btnCarrinho = findViewById(R.id.btnCarrinho);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        BottomNavigationView footer = findViewById(R.id.footer_navigation);
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();
            }
            if(item.getItemId() == R.id.menu_forum){
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_main, new Forum());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentForum()).commit();
            }
            return false;
        });

        footer.setSelectedItemId(R.id.menu_home);

        // Clicar no header e ir para a tela de perfil
        headerView.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        });

    }
}
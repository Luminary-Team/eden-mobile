package com.eden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Sidebar extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        // SIDEBAR DRAWER
        ImageView btnSidebar = findViewById(R.id.sidebarBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.sidebar_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSidebar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_favoritos) {
                    Toast.makeText(Sidebar.this, "Favoritos", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_pontos) {
                    Toast.makeText(Sidebar.this, "Pontos de Coleta", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_artigos) {
                    Toast.makeText(Sidebar.this, "Artigos", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_config) {
                    Toast.makeText(Sidebar.this, "Configurações", Toast.LENGTH_SHORT).show();
                }
//                switch (item.getItemId()) {
//                    case R.id.nav_favoritos:
//                        Toast.makeText(Sidebar.this, "Favoritos", Toast.LENGTH_SHORT).show();
//                    case R.id.nav_pontos:
//                        Toast.makeText(Sidebar.this, "Pontos de Coleta", Toast.LENGTH_SHORT).show();
//                    case R.id.nav_artigos:
//                        Toast.makeText(Sidebar.this, "Artigos", Toast.LENGTH_SHORT).show();
//                    case R.id.nav_config:
//                        Toast.makeText(Sidebar.this, "Configurações", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
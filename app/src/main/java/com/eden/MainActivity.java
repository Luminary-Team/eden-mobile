package com.eden;

import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ImageView btnCarrinho;
    private DrawerLayout drawerLayout;
    private BottomNavigationView footer;
    private NavigationView navView;
    private View headerView;
    private ShapeableImageView profilePic;
    private ImageView btnSidebar;

    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        authListener = firebaseAuth -> {
            if (auth.getCurrentUser() != null) {
                // Setting the perfil photo
                AndroidUtil.downloadImageFromFirebase(this, btnSidebar);
                AndroidUtil.downloadImageFromFirebase(this, profilePic);
            } else {
                Log.d("login", "Current User não encontrado: " + auth.getCurrentUser());
            }
        };

        auth.addAuthStateListener(authListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCarrinho = findViewById(R.id.btnCarrinho);
        drawerLayout = findViewById(R.id.drawer_layout);
        footer = findViewById(R.id.footer_navigation);
        navView = findViewById(R.id.nav_view);
        headerView = navView.getHeaderView(0);
        profilePic = headerView.findViewById(R.id.profile_pic);
        btnSidebar = findViewById(R.id.btnSidebar);

        // Fragmento inicial
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();

        // Botão do sidebar
        btnSidebar.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Sidebar configuration
        navView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.nav_favoritos)
                openActivity(this, FavoritesActivity.class);
            if (menuItem.getItemId() == R.id.nav_pontos)
                openActivity(this, MapsActivityTeste.class);
            if (menuItem.getItemId() == R.id.nav_artigos)
                openActivity(this, ArticlesActivity.class);

            // Closes drawers after selection
            drawerLayout.closeDrawers();

            return true;
        });

        // Navigation footer configuration
        footer.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.menu_add){
                openActivity(this, RegisterProduct.class);
            }
            if(item.getItemId() == R.id.menu_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();
            }
            if(item.getItemId() == R.id.menu_forum){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentForum()).commit();
            }
            return false;
        });

        footer.setSelectedItemId(R.id.menu_home);

        // Cart button
        btnCarrinho.setOnClickListener(v -> openActivity(this, CartActivity.class));

        // Header configuration
        headerView.setOnClickListener(v -> {
            openActivity(this, UserProfile.class);
            // Closes drawers after selection
            drawerLayout.closeDrawers();
            finish();
        });

    }
}
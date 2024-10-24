package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eden.R;
import com.eden.ui.fragments.FragmentForum;
import com.eden.ui.fragments.FragmentHome;
import com.eden.utils.AndroidUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ShapeableImageView profilePic;
    private ImageView btnSidebar;
    private TextView name, username;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Setting the perfil photo
        FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
            // If the user is already logged in
            if (auth.getCurrentUser() != null) {

                // Setting the perfil photo
                AndroidUtil.downloadProfilePicFromFirebase(this, btnSidebar);
                AndroidUtil.downloadProfilePicFromFirebase(this, profilePic);

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

        ImageView btnCarrinho = findViewById(R.id.btnCarrinho);
        BottomNavigationView footer = findViewById(R.id.footer_navigation);
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawer_layout);
        profilePic = headerView.findViewById(R.id.profile_pic);
        btnSidebar = findViewById(R.id.btnSidebar);
        name = headerView.findViewById(R.id.profile_name);
        username = headerView.findViewById(R.id.profile_username);

        // Fragmento inicial
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();

        // Botão do sidebar
        btnSidebar.setOnClickListener(v -> {
            // Setting name and username on the sidebar header
            name.setText(currentUser.getName());
            username.setText(currentUser.getUserName());

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
                openActivity(this, EcoPoint.class);
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
        });

    }
}
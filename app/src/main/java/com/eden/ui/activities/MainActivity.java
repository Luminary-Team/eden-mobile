package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eden.R;
import com.eden.ui.fragments.FragmentForum;
import com.eden.ui.fragments.FragmentHome;
import com.eden.utils.AndroidUtil;
import com.eden.utils.NotificationHelper;
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
    protected void onResume() {
        super.onResume();


        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Setting the perfil photo
        FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
            // If the user is already logged in
            if (auth.getCurrentUser() != null && currentUser != null) {

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


        // Cria o canal de notificação
        NotificationHelper.createNotificationChannel(this);
        NotificationHelper.sendRandomNotification(this);

        sendNotification(getApplicationContext(), this);

        if (currentUser != null) {
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
        }

        // Sidebar configuration
        navView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.nav_favoritos)
                openActivity(this, FavoritesActivity.class);
            if (menuItem.getItemId() == R.id.nav_pontos)
                openActivity(this, EcoPoint.class);
            if (menuItem.getItemId() == R.id.nav_artigos)
                openActivity(this, ArticlesActivity.class);
            if (menuItem.getItemId() == R.id.nav_comprados)
                openActivity(this, BoughtProducts.class);
            if (menuItem.getItemId() == R.id.nav_restricted_area)
                openActivity(this, RestrictedArea.class);
            if (menuItem.getItemId() == R.id.nav_my_products)
                openActivity(this, UserProfile.class);
            if (menuItem.getItemId() == R.id.nav_my_posts)
                openActivity(this, UserProfile.class);

            // Closes drawers after selection
            drawerLayout.closeDrawers();

            return true;
        });

        // Navigation footer configuration
        footer.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_add){
                openActivity(this, RegisterProduct.class);
                footer.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.home_outlined_icon);
                footer.getMenu().findItem(R.id.menu_forum).setIcon(R.drawable.people_outline_icon);
            }
            if(item.getItemId() == R.id.menu_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();
                item.setIcon(R.drawable.home_icon);
                footer.getMenu().findItem(R.id.menu_forum).setIcon(R.drawable.people_outline_icon);
            }
            if(item.getItemId() == R.id.menu_forum){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentForum()).commit();
                item.setIcon(R.drawable.people_icon);
                footer.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.home_outlined_icon);
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


    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private boolean allPermissionsGranted(Context context) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    public void sendNotification(Context context, Context localContext) {

        // Create Notification
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(localContext, "channel_id")
                .setSmallIcon(R.drawable.eden_logotipo_2)
                .setContentTitle("Notification Title")
                .setContentText("CLICK AND RECEIVE!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Create Notification Channel
        NotificationChannel channel = new NotificationChannel("channel_id", "Notify",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Show notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(localContext);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle missing permission
            return;
        }
        notificationManagerCompat.notify(1, builder.build());
    }
}
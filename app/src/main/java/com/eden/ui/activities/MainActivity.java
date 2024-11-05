package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.favorites;
import static com.eden.utils.AndroidUtil.openActivity;
import static com.eden.utils.AndroidUtil.token;
import static com.eden.utils.NotificationHelper.sendRandomNotification;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
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

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ShapeableImageView profilePic;
    private ImageView btnSidebar;
    private TextView name, username;

    private ScheduledExecutorService scheduler;

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

    @SuppressLint("SetTextI18n")
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

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

        // Sets a timer for the notification
        scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> sendRandomNotification(this), 10, TimeUnit.SECONDS);

//        sendNotification(getApplicationContext(), this);

        if (currentUser != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentHome()).commit();

            btnSidebar.setOnClickListener(v -> {
                name.setText(currentUser.getName());
                username.setText("@" + currentUser.getUserName());

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // Sidebar configuration
        navView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.nav_profile)
                openActivity(this, UserProfile.class);
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
//            if (menuItem.getItemId() == R.id.nav_my_posts)
//                openActivity(this, UserProfile.class);

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

        // Logout
        (findViewById(R.id.button_logout)).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            currentUser = null;
            favorites = null;
            token = null;
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    NotificationHelper.createNotificationChannel(this);
                    scheduleRandomNotification(3);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.e("Notification", "PERMISSÃO NEGADA");
                }
            });

    private void scheduleRandomNotification(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            // Generate a random time between 10:00 and 21:00
            int randomTime = random.nextInt( 10);
            scheduler.schedule(() -> sendRandomNotification(this), randomTime, TimeUnit.HOURS);
        }
    }

//    public void sendNotification(Context context, Context localContext) {
//
//        // Create Notification
//        Intent intent = new Intent(context, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(localContext, "channel_id")
//                .setSmallIcon(R.drawable.eden_logotipo_2)
//                .setContentTitle("Notification Title")
//                .setContentText("CLICK AND RECEIVE!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//
//        // Create Notification Channel
//        NotificationChannel channel = new NotificationChannel("channel_id", "Notify",
//                NotificationManager.IMPORTANCE_HIGH);
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(channel);
//
//        // Show notification
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(localContext);
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Handle missing permission
//            return;
//        }
//        notificationManagerCompat.notify(1, builder.build());
//    }
}
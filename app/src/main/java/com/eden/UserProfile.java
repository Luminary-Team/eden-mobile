package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        NavigationView navView = findViewById(R.id.nav_profile);

//        navView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.menu_product) {
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile_produtos);
//            }
//            if (item.getItemId() == R.id.menu_posts) {
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile_posts);
//            }
//        });


    }
}
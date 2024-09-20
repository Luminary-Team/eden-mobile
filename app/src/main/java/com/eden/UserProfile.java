package com.eden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eden.adapter.ProductAdapter;
import com.eden.utils.AndroidUtil;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView btnProfileProdutos = findViewById(R.id.btn_profile_produtos);
        TextView btnProfilePublicacoes = findViewById(R.id.btn_profile_publicacoes);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_profile);
        RelativeLayout headerProfile = findViewById(R.id.header_profile);
        ShapeableImageView profilePic = findViewById(R.id.profile_pic);

        // Adicionando foto de perfil
        AndroidUtil.downloadImageFromFirebase(this, "ProfilePic", profilePic);

        headerProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileEdit.class);
            startActivity(intent);
        });

//        navView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.menu_product) {
//                recyclerView.setAdapter(new ProductAdapter(new ArrayList<>()));
//            }
//            if (item.getItemId() == R.id.menu_posts) {
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile_posts);
//            }
//        });


    }
}
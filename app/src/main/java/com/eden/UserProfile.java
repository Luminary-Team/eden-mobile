package com.eden;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eden.adapter.ProductAdapter;
import com.eden.adapter.ViewPagerAdapter;
import com.eden.utils.AndroidUtil;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    ShapeableImageView profilePic;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Setting tabs
        tabLayout = findViewById(R.id.tabLayout_profile);
        viewPager2 = findViewById(R.id.viewPager_profile);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });



        // Setting Values
        TextView name = findViewById(R.id.profile_name);
        TextView username = findViewById(R.id.profile_username);
        RatingBar rating = findViewById(R.id.profile_rating);

        if (currentUser != null) {
            name.setText(currentUser.getName());
            username.setText(currentUser.getUserName());
            rating.setRating(currentUser.getRating());
        }



        // Setting header
        RelativeLayout headerProfile = findViewById(R.id.header_profile);

        // Setting perfil photo
        profilePic = findViewById(R.id.profile_pic);
        AndroidUtil.downloadProfilePicFromFirebase(this, profilePic);

        headerProfile.setOnClickListener(v -> {
            openActivity(this, UserProfileEdit.class);
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

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
package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.currentUser;
import static com.eden.utils.AndroidUtil.favorites;
import static com.eden.utils.AndroidUtil.openActivity;
import static com.eden.utils.AndroidUtil.token;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eden.R;
import com.eden.adapter.ViewPagerAdapter;
import com.eden.api.dto.UserSchema;
import com.eden.utils.AndroidUtil;
import com.eden.utils.callbacks.UserCallback;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    protected void onResume() {
        super.onResume();

        AndroidUtil.getUser(new UserCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void setResponse(UserSchema response) {

                // Setting Values
                TextView name = findViewById(R.id.profile_name);
                TextView username = findViewById(R.id.profile_username);
                RatingBar rating = findViewById(R.id.profile_rating);

                if (currentUser != null) {
                    name.setText(currentUser.getName());
                    username.setText("@" + currentUser.getUserName());
                    rating.setRating(currentUser.getRating());
                }

                // Setting perfil photo
                profilePic = findViewById(R.id.profile_pic);
                AndroidUtil.downloadProfilePicFromFirebase(UserProfile.this, profilePic);

                // Open the editUser activity
                (findViewById(R.id.header_profile)).setOnClickListener(v -> openActivity(UserProfile.this, UserProfileEdit.class));

            }
        });

    }
}
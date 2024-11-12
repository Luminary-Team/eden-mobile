package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.authenticate;
import static com.eden.utils.AndroidUtil.getToken;
import static com.eden.utils.AndroidUtil.openActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.eden.R;
import com.eden.utils.AndroidUtil;
import com.eden.utils.FirebaseUserUtil;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();

        if(FirebaseUserUtil.isLoggedIn()) {
            // Goes straight to main
            authenticate(SplashScreen.this);
        } else {
            // Wait 2 seconds and go to login
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openActivity(SplashScreen.this, UserLogin.class);
                    finish();
                }
            }, 2000);
        }
    }
}
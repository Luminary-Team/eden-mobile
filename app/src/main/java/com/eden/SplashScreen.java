package com.eden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.eden.utils.FirebaseUserUtil;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseUserUtil.isLoggedIn()) {
                    entrarApp(MainActivity.class);
                } else {
                    entrarApp(UserLogin.class);
                }
            }
        }, 2500);
    }
    public void entrarApp(Class<?> context) {
        Intent intent = new Intent(this, context);
        startActivity(intent);
        finish();
    }
}
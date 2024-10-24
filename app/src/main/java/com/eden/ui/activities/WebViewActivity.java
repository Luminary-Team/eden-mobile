package com.eden.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.eden.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // Para abrir links dentro da WebView

        // Obter a URL passada pela Intent
        String url = "https://eden-restrict-area.onrender.com/";
        if (url != null) {
            webView.loadUrl(url);
        }

    }
}
package com.maid.gardeningfriend.inicio;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.login.Login;
import com.maid.gardeningfriend.registro.RegistroActivity;

/**
 * Presentacion de la seccion de inicio
 */
public class Inicio extends MainActivity {
    Button toLogin, toRegister;
    TextView textViewRegisterOrLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Configurar video de YT
        WebView webView = findViewById(R.id.youtube_video);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/uniGzuqH3nE?si=pK6WeV2iuGxm98ru\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        toLogin = findViewById(R.id.btnLoginInicio);
        toRegister = findViewById(R.id.btnRegisterInicio);
        textViewRegisterOrLogin = findViewById(R.id.textViewLoginToAllFunctions);

        if (auth.getCurrentUser() != null){
            toLogin.setVisibility(View.GONE);
            toRegister.setVisibility(View.GONE);
            textViewRegisterOrLogin.setVisibility(View.GONE);
        }else {
            toLogin.setVisibility(View.VISIBLE);
            toRegister.setVisibility(View.VISIBLE);
            textViewRegisterOrLogin.setVisibility(View.VISIBLE);
        }

        toLogin.setOnClickListener((v)->{
            Intent intent = new Intent(Inicio.this, Login.class);
            startActivity(intent);
        });

        toRegister.setOnClickListener((v)->{
            Intent intent = new Intent(Inicio.this, RegistroActivity.class);
            startActivity(intent);
        });

    }
}
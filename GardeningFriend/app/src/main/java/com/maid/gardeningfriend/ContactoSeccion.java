package com.maid.gardeningfriend;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ContactoSeccion extends MainActivity {
    ImageView imgFacebook, imgTwitter, imgInstagram, imgLinkedin, imgGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_seccion);

        imgFacebook = findViewById(R.id.imageViewFacebook);
        imgTwitter = findViewById(R.id.imageViewTwitter);
        imgInstagram = findViewById(R.id.imageViewInstagram);
        imgLinkedin = findViewById(R.id.imageViewLinkedin);
        imgGithub = findViewById(R.id.imageViewGithub);

        imgFacebook.setOnClickListener((v)-> goToLink("https://www.facebook.com/"));
        imgTwitter.setOnClickListener((v)-> goToLink("https://twitter.com/"));
        imgInstagram.setOnClickListener((v)-> goToLink("https://www.instagram.com/"));
        imgLinkedin.setOnClickListener((v)-> goToLink("https://ar.linkedin.com/"));
        imgGithub.setOnClickListener((v)-> goToLink("https://github.com/GardeningFriendTeam/GardeningFriendApp"));

    }

    private void goToLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
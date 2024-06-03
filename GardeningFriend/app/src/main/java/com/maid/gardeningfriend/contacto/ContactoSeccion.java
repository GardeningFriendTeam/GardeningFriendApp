package com.maid.gardeningfriend.contacto;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

public class ContactoSeccion extends MainActivity {
    ImageView imgFacebook, imgTwitter, imgInstagram, imgLinkedin, imgGithub;
    Button buttonWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_contacto_seccion, findViewById(R.id.content_frame));

        imgFacebook = findViewById(R.id.imageViewFacebook);
        imgTwitter = findViewById(R.id.imageViewTwitter);
        imgInstagram = findViewById(R.id.imageViewInstagram);
        imgLinkedin = findViewById(R.id.imageViewLinkedin);
        imgGithub = findViewById(R.id.imageViewGithub);
        buttonWeb = findViewById(R.id.buttonWeb);

        imgFacebook.setOnClickListener((v)-> goToLink("https://www.facebook.com/"));
        imgTwitter.setOnClickListener((v)-> goToLink("https://twitter.com/"));
        imgInstagram.setOnClickListener((v)-> goToLink("https://www.instagram.com/"));
        imgLinkedin.setOnClickListener((v)-> goToLink("https://ar.linkedin.com/"));
        imgGithub.setOnClickListener((v)-> goToLink("https://github.com/GardeningFriendTeam/GardeningFriendApp"));
        buttonWeb.setOnClickListener((v) -> goToLink("https://github.com/GardeningFriendTeam/GardeningFriendWeb"));

    }

    private void goToLink(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
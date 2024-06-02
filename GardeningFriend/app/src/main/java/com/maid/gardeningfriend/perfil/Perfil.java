package com.maid.gardeningfriend.perfil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.login.Login;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Perfil extends MainActivity {
    public static final String TAG = "TAG";
    TextView usernameProfile, emailProfile;
    Button btnLogout, btnEditProfile, btnVerifyEmail;
    ImageView profileImage, imageButtonVerifyEmail;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    FirebaseUser mUser;
    StorageReference storageReference;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_perfil, findViewById(R.id.content_frame));


        usernameProfile = findViewById(R.id.textViewUsernameProfile);
        emailProfile = findViewById(R.id.textViewEmailProfile);
        btnLogout = findViewById(R.id.btnLogoutProfile);
        profileImage = findViewById(R.id.imageViewProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        imageButtonVerifyEmail = findViewById(R.id.imageBtnVerifyEmail);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("usuarios/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        userId = mAuth.getCurrentUser().getUid();

        if (!mUser.isEmailVerified()){
            imageButtonVerifyEmail.setVisibility(View.VISIBLE);
            btnVerifyEmail.setVisibility(View.VISIBLE);

            imageButtonVerifyEmail.setOnClickListener((v -> Toast.makeText(this, "Tu Email aún no esta verificado.", Toast.LENGTH_SHORT).show()));

            btnVerifyEmail.setOnClickListener(v -> mUser.sendEmailVerification().addOnSuccessListener(unused -> Toast.makeText(Perfil.this, "El email de verificacion ha sido enviado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d(TAG, "onFailure: Email no enviado")));
        }


        DocumentReference documentReference = mFirestore.collection("usuarios").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
            assert documentSnapshot != null;
            usernameProfile.setText(documentSnapshot.getString("name"));
            emailProfile.setText(documentSnapshot.getString("email"));
        });


        profileImage.setOnClickListener(v -> {
            // Abrir galeria
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        btnEditProfile.setOnClickListener((v)-> {
            Intent intent = new Intent(v.getContext(), EditarPerfilActivity.class);
            intent.putExtra("username", usernameProfile.getText().toString());
            intent.putExtra("email", emailProfile.getText().toString());
            startActivity(intent);
        });


        btnLogout.setOnClickListener((v) -> logout());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Subir imagen a Firebase Storage.
        StorageReference fileRef = storageReference.child("usuarios/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // La imagen se subió exitosamente, puedes cargarla en la vista de perfil
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));
            Toast.makeText(this, "Se cambio la foto de perfil", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(Perfil.this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show());
    }


    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(Perfil.this, Login.class));
        finish();
    }
}
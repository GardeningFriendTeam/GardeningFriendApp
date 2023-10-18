package com.maid.gardeningfriend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Perfil extends MainActivity {
    public static final String TAG = "TAG";
    TextView usernameProfile, emailProfile;
    ImageButton imageButtonVerifyEmail;
    Button btnLogout, btnEditProfile, btnChangePassword, btnVerifyEmail;
    ImageView profileImage;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    FirebaseUser mUser;
    StorageReference storageReference;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        usernameProfile = findViewById(R.id.textViewUsernameProfile);
        emailProfile = findViewById(R.id.textViewEmailProfile);
        btnLogout = findViewById(R.id.btnLogoutProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
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

        btnChangePassword.setOnClickListener(v -> {
            final EditText resetPassword = new EditText(v.getContext());
            final AlertDialog.Builder alertDialogResetPassword = new AlertDialog.Builder(v.getContext());
            alertDialogResetPassword.setTitle("¿Cambiar Contraseña?");
            alertDialogResetPassword.setMessage("Ingrese su nueva contraseña");
            alertDialogResetPassword.setView(resetPassword);

            alertDialogResetPassword.setPositiveButton("Si", (dialog, which) -> {
                // Extrae el Email y envía el enlace
                String newPassword = resetPassword.getText().toString();

                // Validación de la nueva contraseña
                if (isValidPassword(newPassword)) {
                    mUser.updatePassword(newPassword).addOnSuccessListener(unused ->
                            Toast.makeText(Perfil.this, "Contraseña cambiada exitosamente",
                                    Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                            Toast.makeText(Perfil.this, "Error al cambiar la contraseña " + e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(Perfil.this, "La contraseña debe tener al menos 8 caracteres y contener letras, números y caracteres especiales", Toast.LENGTH_SHORT).show();
                }

            });
            alertDialogResetPassword.setNegativeButton("No", (dialog, which) -> {
                // Cierra el cuadro de diálogo
            });
            alertDialogResetPassword.create().show();
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

    // Función para validar la nueva contraseña
    private boolean isValidPassword(String password) {
        // La contraseña debe tener al menos 8 caracteres
        if (password.length() < 8) {
            return false;
        }
        // La contraseña debe contener una mezcla de letras, números y caracteres especiales
        // Puedes personalizar esta expresión regular según tus necesidades
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }

    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(Perfil.this, Login.class));
        finish();
    }
}
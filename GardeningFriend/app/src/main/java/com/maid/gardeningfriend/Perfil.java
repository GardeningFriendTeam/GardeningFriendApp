package com.maid.gardeningfriend;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Perfil extends MainActivity {
    public static final String TAG = "TAG";
    TextView usernameProfile, emailProfile, textViewVerifyEmail;
    Button btnLogout, btnEditProfile, btnChangePassword, btnVerifyEmail;
    FloatingActionButton updateProfileImg;
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
        updateProfileImg = findViewById(R.id.uploadImageProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);
        textViewVerifyEmail = findViewById(R.id.textViewVerifyEmail);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("usuarios/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userId = mAuth.getCurrentUser().getUid();

        if (!mUser.isEmailVerified()){
            textViewVerifyEmail.setVisibility(View.VISIBLE);
            btnVerifyEmail.setVisibility(View.VISIBLE);

            btnVerifyEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Perfil.this, "El email de verificacion ha sido enviado", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email no enviado");
                        }
                    });
                }
            });
        }


        DocumentReference documentReference = mFirestore.collection("usuarios").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                usernameProfile.setText(documentSnapshot.getString("name"));
                emailProfile.setText(documentSnapshot.getString("email"));
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder alertDialogResetPassword = new AlertDialog.Builder(v.getContext());
                alertDialogResetPassword.setTitle("Cambiar Contrasena");
                alertDialogResetPassword.setMessage("Ingrese su nueva contrasena mayor a 8 digitos");
                alertDialogResetPassword.setView(resetPassword);

                alertDialogResetPassword.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extrae el Email y envia el link
                        String newPassword = resetPassword.getText().toString();
                        mUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Perfil.this, "Contrasena cambiada exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Perfil.this, "Error al cambiar la contrasena "+e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                alertDialogResetPassword.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cierra el Dialog
                    }
                });
                alertDialogResetPassword.create().show();
            }
        });

        updateProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir galeria
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
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
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Subir imagen a firebase storage.
        StorageReference fileRef = storageReference.child("usuarios/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Perfil.this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(Perfil.this, Login.class));
        finish();
    }
}
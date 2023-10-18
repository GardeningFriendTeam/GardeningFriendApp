package com.maid.gardeningfriend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditarPerfilActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText profileUsername, profileEmail;
    ImageView profileImage;
    Button saveBtn;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Intent data = getIntent();
        String username = data.getStringExtra("username");
        String email = data.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (mUser == null) {
            // Si el usuario no ha iniciado sesión, redirige a la pantalla de inicio de sesión
            Intent intent = new Intent(this, Login.class); // Reemplaza LoginActivity con el nombre de tu actividad de inicio de sesión
            startActivity(intent);
            finish(); // Cierra la actividad actual
        }

        profileUsername = findViewById(R.id.editTextEditProfileUsername);
        profileEmail = findViewById(R.id.editTextEditProfileEmail);
        profileImage = findViewById(R.id.imageViewEditProfile);
        saveBtn = findViewById(R.id.btnSaveEditProfile);

        StorageReference profileRef = storageReference.child("usuarios/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));
        profileImage.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        saveBtn.setOnClickListener(v -> {
            String editUsername = profileUsername.getText().toString();
            String editEmail = profileEmail.getText().toString();
            if (editUsername.isEmpty() || editEmail.isEmpty()){
                Toast.makeText(EditarPerfilActivity.this, "Uno o mas campos estan vacios.", Toast.LENGTH_SHORT).show();
                return;
            } else if (editUsername.length() < 3) {
                Toast.makeText(this, "Username muy corto", Toast.LENGTH_SHORT).show();
                profileUsername.setError("Username debe ser de al menos 3 caracteres");
                profileUsername.requestFocus();
                return;
            } else if (!isValidEmail(editEmail)) {
                Toast.makeText(EditarPerfilActivity.this, "Email invalido", Toast.LENGTH_SHORT).show();
                profileEmail.setError("El correo electrónico no es válido.");
                profileEmail.requestFocus();
                return;
            }

            mUser.updateEmail(editEmail).addOnSuccessListener(unused -> {
                DocumentReference documentReference = mStore.collection("usuarios").document(mUser.getUid());
                Map<String, Object> edited = new HashMap<>();
                edited.put("email", editEmail);
                edited.put("name", editUsername);
                documentReference.update(edited).addOnSuccessListener(unused1 -> {
                    Toast.makeText(EditarPerfilActivity.this, "Perfil modificado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                    finish();
                });
            }).addOnFailureListener(e ->
                    Toast.makeText(EditarPerfilActivity.this, "Fallo en modificar el email "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        });

        profileUsername.setText(username);
        profileEmail.setText(email);

        Log.d(TAG, "onCreate: " + username +" "+ email);
    }

    // Método para verificar si un correo electrónico es válido
    private boolean isValidEmail(String email) {
        // Aquí puedes agregar tu lógica de validación de correo electrónico
        // Puedes usar una expresión regular o cualquier otra validación que prefieras
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
        }).addOnFailureListener(e -> Toast.makeText(EditarPerfilActivity.this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show());
    }

}
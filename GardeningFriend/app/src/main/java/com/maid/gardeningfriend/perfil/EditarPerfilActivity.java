package com.maid.gardeningfriend.perfil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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
import com.maid.gardeningfriend.login.Login;
import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditarPerfilActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText profileUsername, profileEmail, profilePassword;
    ImageView profileImage, showPassword;
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
        profilePassword = findViewById(R.id.editTextEditProfilePassword);
        showPassword = findViewById(R.id.showPasswordIcon);
        saveBtn = findViewById(R.id.btnSaveEditProfile);

        StorageReference profileRef = storageReference.child("usuarios/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));
        profileImage.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        // Mostrar / ocultar password
        showPassword.setOnClickListener((v -> {
            // Alterna la visibilidad de la contraseña al tocar el ícono
            if (profilePassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                profilePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //showPassword.setImageResource(R.drawable.ic_show_password);
            } else {
                profilePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                //showPassword.setImageResource(R.drawable.ic_hide_password);
            }

            // Mueve el cursor al final del texto
            profilePassword.setSelection(profilePassword.getText().length());
        }));

        saveBtn.setOnClickListener(v -> {
            String editUsername = profileUsername.getText().toString();
            String editEmail = profileEmail.getText().toString();
            String editPassword = profilePassword.getText().toString();

            if (editUsername.isEmpty()){
                Toast.makeText(EditarPerfilActivity.this, "Un username es requerido", Toast.LENGTH_SHORT).show();
                profileUsername.setError("Completa el campo username");
                profileUsername.requestFocus();
                return;
            } else if (editUsername.length() < 3) {
                Toast.makeText(this, "Username muy corto", Toast.LENGTH_SHORT).show();
                profileUsername.setError("Username debe ser de al menos 3 caracteres");
                profileUsername.requestFocus();
                return;
            } else if (editUsername.length() > 30) {
                Toast.makeText(this, "Username muy largo", Toast.LENGTH_SHORT).show();
                profileUsername.setError("Username debe contener menos de 30 caracteres");
                profileUsername.requestFocus();
                return;
            }

            if (!editPassword.isEmpty()){
                // Validación de la nueva contraseña
                if (isValidPassword(editPassword)) {
                    mUser.updatePassword(editPassword).addOnSuccessListener(unused ->
                            Toast.makeText(EditarPerfilActivity.this, "Contraseña cambiada exitosamente",
                                    Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                            Toast.makeText(EditarPerfilActivity.this, "Error al cambiar la contraseña " + e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
                    profilePassword.setError("La contraseña debe tener al menos 8 caracteres y contener letras, números y caracteres especiales");
                    profilePassword.requestFocus();
                    return;
                }
            }

            if (!mUser.isEmailVerified()){
                Toast.makeText(this, "Recuerda validar tu email", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarPerfilActivity.this, "Fallo en modificar el perfil "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        });

        profileUsername.setText(username);
        profileEmail.setText(email);

        Log.d(TAG, "onCreate: " + username +" "+ email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imageUri = data.getData();
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

}
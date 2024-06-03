package com.maid.gardeningfriend.registro;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.login.Login;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.perfil.Perfil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistroActivity extends MainActivity {
    Button btn_registro;
    // Declaración de elementos de la interfaz de usuario
    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBarRegistro;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegistroActivity";
    private final ArrayList<String> favoritos = new ArrayList<>();
    private final boolean isAdmin = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //getLayoutInflater().inflate(R.layout.activity_registro, findViewById(R.id.content_frame));

        //Cambiando el titulo de la ActionBar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registro");

        // Inicialización de instancias de Firebase Firestore y FirebaseAuth
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Vinculación de elementos de la interfaz con variables
        editTextName = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_registro = findViewById(R.id.buttonSignup);
        progressBarRegistro = findViewById(R.id.progressBarRegistro);
        ImageView showPassword = findViewById(R.id.showPasswordIcon);
        TextView textViewGoToLogin = findViewById(R.id.textViewGoToLogin);

        // Evento onClick para redirigir a la Activity Login
        textViewGoToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegistroActivity.this, Login.class);
            startActivity(intent);
        });

        // Mostrar / ocultar password
        showPassword.setOnClickListener((v -> {
            // Alterna la visibilidad de la contraseña al tocar el ícono
            if (editTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //showPassword.setImageResource(R.drawable.ic_show_password);
            } else {
                editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                //showPassword.setImageResource(R.drawable.ic_hide_password);
            }

            // Mueve el cursor al final del texto
            editTextPassword.setSelection(editTextPassword.getText().length());
        }));

        // Expresión regular para validar la contraseña
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        // Configuración del evento onClick para el botón de registro
        btn_registro.setOnClickListener(view -> {
            // Obtención de valores ingresados por el usuario
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            // Validaciones de campos
            if (TextUtils.isEmpty(name)){
                Toast.makeText(RegistroActivity.this, "Completa el campo username", Toast.LENGTH_SHORT).show();
                editTextName.setError("Username es requerido");
                editTextName.requestFocus();
            }else if (name.length() < 3){
                Toast.makeText(RegistroActivity.this, "El username debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
                editTextName.setError("Username muy corto");
                editTextName.requestFocus();
            }else if (name.length() > 30){
                Toast.makeText(RegistroActivity.this, "El username debe tener menos de 30 caracteres", Toast.LENGTH_SHORT).show();
                editTextName.setError("Username muy largo");
                editTextName.requestFocus();
            }else if (TextUtils.isEmpty(email)){
                Toast.makeText(RegistroActivity.this, "Completa el campo email", Toast.LENGTH_SHORT).show();
                editTextEmail.setError("Email es requerido");
                editTextEmail.requestFocus();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(RegistroActivity.this, "Re-ingresa el campo email", Toast.LENGTH_SHORT).show();
                editTextEmail.setError("Un email valido es requerido");
                editTextEmail.requestFocus();
            }else if (TextUtils.isEmpty(password)){
                Toast.makeText(RegistroActivity.this, "Completa el campo password", Toast.LENGTH_SHORT).show();
                editTextPassword.setError("Contraseña es requerida");
                editTextPassword.requestFocus();
            }else if (!password.matches(passwordRegex)){
                Toast.makeText(RegistroActivity.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                editTextPassword.setError("La contraseña debe tener al menos 8 dígitos y contener letras, números y caracteres especiales");
                editTextPassword.requestFocus();
            }else {
                // Si los campos son válidos, se muestra una barra de progreso y se procede al registro
                progressBar(true);
                registerUser(name, email, password);
            }
        });
    }

    // Método para registrar un usuario en Firebase Authentication y Firestore
    private void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // Si el registro es exitoso, se obtiene el usuario actual y se guarda información en Firestore
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                map.put("favoritos", favoritos);
                map.put("isAdmin", isAdmin);

                // Se envía una verificación por correo electrónico
                assert firebaseUser != null;
                firebaseUser.sendEmailVerification();

                // Se almacenan los datos del usuario en Firestore
                mFirestore.collection("usuarios").document(id).set(map).addOnSuccessListener(unused -> {
                    // Se abre el perfil del usuario después de un registro exitoso
                    Intent intent = new Intent(RegistroActivity.this, Perfil.class);
                    // Para prevenir que el usuario vuelva a RegistroActivity presionando el botón "back" después de registrarse
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Cierra la Activity
                    Toast.makeText(RegistroActivity.this, "Registro Exitoso! Por favor valide su Email.", Toast.LENGTH_SHORT).show();
                    progressBar(false);
                });
            }else {
                // Manejo de excepciones relacionadas con Firebase Authentication
                try {
                    throw Objects.requireNonNull(task.getException());
                }catch (FirebaseAuthWeakPasswordException e){
                    editTextPassword.setError("Tu contraseña es muy debil, " +
                            "intenta usar una combinación de letras, números y caracteres especiales.");
                    editTextPassword.requestFocus();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    editTextEmail.setError("Tu email es invalido o ya esta en uso, intenta de nuevo.");
                    editTextEmail.requestFocus();
                }catch (FirebaseAuthUserCollisionException e){
                    editTextEmail.setError("El usuario ya esta registrado con este Email, intenta con otro Email.");
                    editTextEmail.requestFocus();
                }catch (Exception e){
                    Log.e(TAG, "Error: "+ e.getMessage());
                    Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar(false);
            }

        });
    }

    private void progressBar(boolean isVisible){
        if (isVisible){
            progressBarRegistro.setVisibility(View.VISIBLE);
            btn_registro.setVisibility(View.GONE);
        }else {
            progressBarRegistro.setVisibility(View.GONE);
            btn_registro.setVisibility(View.VISIBLE);
        }
    }

}

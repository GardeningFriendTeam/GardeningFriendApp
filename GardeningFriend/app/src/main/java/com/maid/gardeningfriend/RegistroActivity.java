package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistroActivity extends MainActivity {
    Button btn_registro;

    private EditText editTextName, editTextEmail, editTextPassword;
    private TextView textViewGoToLogin;
    private ProgressBar progressBarRegistro;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegistroActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        getSupportActionBar().setTitle("Registro");

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_registro = findViewById(R.id.buttonSignup);
        progressBarRegistro = findViewById(R.id.progressBarRegistro);
        textViewGoToLogin = findViewById(R.id.textViewGoToLogin);

        textViewGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroActivity.this, Login.class);
                startActivity(intent);
                Toast.makeText(RegistroActivity.this, "Login", Toast.LENGTH_SHORT).show();
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegistroActivity.this, "Completa el campo username", Toast.LENGTH_SHORT).show();
                    editTextName.setError("Username es requerido");
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
                }else if (password.length() < 8){
                    Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 8 digitos", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Contraseña muy debil");
                    editTextPassword.requestFocus();
                }else {
                    progressBarRegistro.setVisibility(View.VISIBLE);
                    registerUser(name, email, password);
                }
//                progressBarRegistro.setVisibility(View.GONE);
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    // Envia una verificación por Email
                    firebaseUser.sendEmailVerification();

                    mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Abre el Perfil del usuario luego de un registro exitoso
                            Intent intent = new Intent(RegistroActivity.this, Perfil.class);
                            // Para prevenir que el usuario vuelva a RegistroActivity presionando el boton back despues de registrarse
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // Cierra la Activity
                            Toast.makeText(RegistroActivity.this, "Registro Exitoso! Por favor valide su Email.", Toast.LENGTH_SHORT).show();
                            progressBarRegistro.setVisibility(View.GONE);
                        }
                    });
                }else {
                    try {
                        throw task.getException();
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
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBarRegistro.setVisibility(View.GONE);
                }

            }
        });
    }

}

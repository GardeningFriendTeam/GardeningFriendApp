package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends MainActivity {

    Button btnLogin;
    private EditText editTextEmail, editTextPassword;
    private TextView textViewGoToRegistro;
    private ProgressBar progressBarLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "Login";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        // Vinculación de elementos de la interfaz con variables
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        btnLogin = findViewById(R.id.buttonLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        textViewGoToRegistro = findViewById(R.id.textViewGoToRegistro);

        // Evento onClick para redirigir a la Activity Login
        textViewGoToRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // Expresión regular para validar la contraseña
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        // Configuración del evento onClick para el botón de registro
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtención de valores ingresados por el usuario
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validaciones de campos
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Completa el campo email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email es requerido");
                    editTextEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Login.this, "Re-ingresa el campo email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Un email valido es requerido");
                    editTextEmail.requestFocus();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Completa el campo password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Contraseña es requerida");
                    editTextPassword.requestFocus();
                }else if (!password.matches(passwordRegex)){
                    Toast.makeText(Login.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("La contraseña debe tener al menos 8 dígitos y contener letras, números y caracteres especiales");
                    editTextPassword.requestFocus();
                }else {
                    // Si los campos son válidos, se muestra una barra de progreso y se procede al registro
                    progressBarLogin.setVisibility(View.VISIBLE);
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    if (mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(Login.this, "Login Exitoso", Toast.LENGTH_SHORT).show();
                        // Se abre el perfil del usuario después de un registro exitoso
                        Intent intent = new Intent(Login.this, Perfil.class);
                        // Para prevenir que el usuario vuelva a RegistroActivity presionando el botón "back" después de registrarse
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Cierra la Activity
                        progressBarLogin.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(Login.this, "Email no verificado, por favor valida tu email", Toast.LENGTH_SHORT).show();
                        progressBarLogin.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);
                }
            }
        });
    }
}
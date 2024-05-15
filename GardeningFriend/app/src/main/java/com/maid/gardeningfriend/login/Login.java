package com.maid.gardeningfriend.login;

import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.perfil.Perfil;
import com.maid.gardeningfriend.registro.RegistroActivity;

import java.util.Objects;

public class Login extends MainActivity {

    Button btnLogin;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBarLogin;
    ImageView showPassword;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        // Vinculación de elementos de la interfaz con variables
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        btnLogin = findViewById(R.id.buttonLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        TextView textViewGoToRegistro = findViewById(R.id.textViewGoToRegistro);
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        showPassword = findViewById(R.id.showPasswordIcon);

        // Evento onClick para redirigir a la Activity Registro
        textViewGoToRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, RegistroActivity.class);
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
        btnLogin.setOnClickListener(view -> {
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
                // Si los campos son válidos, se muestra una barra de progreso y se procede al login
                progressBar(true);
                loginUser(email, password);
            }
        });

        textViewForgotPassword.setOnClickListener(v -> {
            EditText resetEmail = new EditText(v.getContext());
            AlertDialog.Builder alertDialogResetPassword = new AlertDialog.Builder(v.getContext());
            alertDialogResetPassword.setTitle("¿Cambiar Contraseña?");
            alertDialogResetPassword.setMessage("Ingrese su Email, se le enviara un link para cambiar su contraseña");
            alertDialogResetPassword.setView(resetEmail);

            alertDialogResetPassword.setPositiveButton("Si", (dialog, which) -> {
                // Extrae el Email y envia el link
                String email = resetEmail.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Login.this, "Email invalido, intentalo de nuevo", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> Toast.makeText(Login.this, "El link fue enviado a tu Email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Login.this, "Error al enviar el Email"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            });
            alertDialogResetPassword.setNegativeButton("No", (dialog, which) -> {
                // Cierra el Dialog
            });
            alertDialogResetPassword.create().show();
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (!Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                    Toast.makeText(Login.this, "Por Favor, Verifica tu email.", Toast.LENGTH_SHORT).show();
                }
                // Se abre el perfil del usuario después de un registro exitoso
                Intent intent = new Intent(Login.this, Perfil.class);
                // Para prevenir que el usuario vuelva a Login presionando el botón "back" después de registrarse
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Cierra la Activity
                progressBar(false);
            }else {
                Toast.makeText(Login.this, "Email o contraseña invalido", Toast.LENGTH_SHORT).show();
                progressBar(false);
            }
        });
    }

    private void progressBar(boolean isVisible){
        if (isVisible){
            progressBarLogin.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }else {
            progressBarLogin.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}
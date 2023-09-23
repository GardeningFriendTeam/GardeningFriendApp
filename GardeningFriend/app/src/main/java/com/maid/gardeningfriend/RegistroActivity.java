package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText editTextUsername;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    Button buttonSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialización de elementos de la interfaz de usuario
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        buttonSignup = findViewById(R.id.buttonSignup);
        mAuth = FirebaseAuth.getInstance();

        // Configuración del evento de clic para el botón de registro
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la entrada del usuario
                String username = Objects.requireNonNull(editTextUsername.getText()).toString().trim();
                String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
                String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();

                // Validar la entrada
                if (isValidInput(username, email, password)) {
                    // Registrar al usuario con Firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro exitoso
                                        Toast.makeText(RegistroActivity.this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();
                                        // TODO: Después de un registro exitoso, se puede realizar acciones adicionales,
                                        //  como iniciar una nueva Activity.
                                    } else {
                                        // Error durante el registro
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        assert e != null;
                                        Toast.makeText(RegistroActivity.this, "Registro fallido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    // Función para validar la entrada del usuario
    private boolean isValidInput(@NonNull String username, @NonNull String email, @NonNull String password) {
        boolean isValid = true;

        if (!isValidUsername(username)) {
            textInputLayoutUsername.setError("El usuario debe contener al menos 3 caracteres");
            isValid = false;
        } else {
            textInputLayoutUsername.setError(null);
        }

        if (!isValidEmail(email)) {
            textInputLayoutEmail.setError("Dirección de email inválida");
            isValid = false;
        } else {
            textInputLayoutEmail.setError(null);
        }

        if (!isStrongPassword(password)) {
            textInputLayoutPassword.setError("La contraseña debe contener al menos 8 caracteres");
            isValid = false;
        } else {
            textInputLayoutPassword.setError(null);
        }

        return isValid;
    }

    // Función para validar el formato del nombre de usuario
    private boolean isValidUsername(String username) {
        return username.length() >= 3;
    }

    // Función para validar el formato de la dirección de email utilizando regex
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.matches(emailPattern, email);
    }

    // Función para verificar la fortaleza de la contraseña
    private boolean isStrongPassword(String password) {
        return password.length() >= 8;
    }
}

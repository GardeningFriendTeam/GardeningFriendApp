package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText editTextUsername;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private Button buttonSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validate input
                if (isValidInput(username, email, password)) {
                    // Registration successful
                    displayToast("Registro Exitoso!");
                    // You can proceed with saving user data or other actions here
                }
            }
        });
    }

    // Function to validate user input
    private boolean isValidInput(@NonNull String username, @NonNull String email, @NonNull String password) {
        if (!isValidUsername(username) && !isValidEmail(email) && !isStrongPassword(password)){
            textInputLayoutUsername.setError("El usuario debe contener minimo 3 caracteres");
            textInputLayoutEmail.setError("Dirección de email invalida");
            textInputLayoutPassword.setError("La contraseña debe contener minimo 8 caracteres");
            return false;
        }else if (!isValidUsername(username)){
            textInputLayoutUsername.setError("El usuario debe contener minimo 3 caracteres");
            return false;
        }else if (!isValidEmail(email)) {
            textInputLayoutEmail.setError("Dirección de email invalida");
            return false;
        }else if (!isStrongPassword(password)) {
            textInputLayoutPassword.setError("La contraseña debe contener minimo 8 caracteres");
            return false;
        }else {
            textInputLayoutUsername.setError(null);
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);

            return true;
        }
    }

    private boolean isValidUsername(String username){
        return username.length() >= 3;
    }
    // Function to validate email format using regex
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.matches(emailPattern, email);
    }
    // Function to check password strength

    private boolean isStrongPassword(String password) {
        return password.length() >= 8;
    }

    // Function to display a Toast message
    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
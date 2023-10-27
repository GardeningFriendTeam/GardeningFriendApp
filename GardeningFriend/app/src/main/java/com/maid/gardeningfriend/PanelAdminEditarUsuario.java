package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PanelAdminEditarUsuario extends MainActivity {
    // Atributos
    String ID;
    String name;
    String email;
    String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_editar_usuario);

        //se extraen las propiedades del intent parceable
        UsuarioParceable usuarioParceable = getIntent().getParcelableExtra("DATOS_USUARIO_PANEL");

        ID = usuarioParceable.getID();
        name = usuarioParceable.getName();
        email = usuarioParceable.getEmail();
        rol = usuarioParceable.getRol();

        //se identifican los campos y se insertan sus respectivos valores:
        TextView nombreUsuario = findViewById(R.id.nombre_user_panel);
        nombreUsuario.setText(name);

        TextView emailUsuario = findViewById(R.id.email_user_panel);
        emailUsuario.setText(email);

        RadioButton RB_admin = findViewById(R.id.RB_admin);
        RadioButton RB_usuario = findViewById(R.id.RB_usuario);
        // se determina el rol definir una opc por default
        if (rol == "usuario"){
            RB_usuario.setChecked(true);
        } else if (rol == "admin"){
            RB_admin.setChecked(true);
        } else {
            RB_usuario.setChecked(true);
        }

    }

    /**
     * verifica que opc fue seleccionada
     * @param view
     * RB que dispara el evento
     */
    public void rolSelec(View view){
        if (view.getId() == R.id.RB_usuario){
            rol = "usuario";

        } else if (view.getId() == R.id.RB_admin){
            rol = "admin";

        } else{
            rol = "usuario";
        }
    }

    /**
     * realiza una UPDATE request en la BD
     * @param view
     * btn que dispara el evento
     */
    public void actualizarUsuario(View view){
        // 1 - se crea instancia de FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // se identifica el documento
        DocumentReference docRef = db.collection("usuarios").document(ID);

        // 2 - se crea un objeto con el valor a actualizar
        Map<String, Object> rolUser = new HashMap<>();
        if (rol == "admin"){
            rolUser.put("isAdmin", true);

        } else if (rol == "usuario"){
            rolUser.put("isAdmin", false);

        } else {
            rolUser.put("isAdmin", false);
        }

        //mensajes de alerta
        Toast msjExito = Toast.makeText(PanelAdminEditarUsuario.this,
                "operacion realizada correctamente",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(PanelAdminEditarUsuario.this,
                "ha ocurrido un error al conectar con la BD",
                Toast.LENGTH_SHORT);

        // 3 - se realiza la UPDATE request pasando el objeto creado previam.
        docRef.update(rolUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        msjExito.show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        msjError.show();
                        Log.d("tag", "error al conectar con la BD: ", e);
                    }
                });
    }
}
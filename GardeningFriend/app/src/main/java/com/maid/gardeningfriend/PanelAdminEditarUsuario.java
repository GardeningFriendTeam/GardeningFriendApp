package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
        TextView emailUsuario = findViewById(R.id.email_user_panel);
        RadioGroup opcionesRoles = findViewById(R.id.opciones_roles);
        RadioButton RB_admin = findViewById(R.id.RB_admin);
        RadioButton RB_usuario = findViewById(R.id.RB_usuario);
        
    }
}
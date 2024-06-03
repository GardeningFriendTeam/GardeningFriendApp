package com.maid.gardeningfriend.panelAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.login.Login;

/**
 * Primera pantalla del panel de admin
 * donde se selecciona si se quiere
 * administrar la coleccion de "cultivos o usuarios"
 */
public class PanelAdmin extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_panel_admin, findViewById(R.id.content_frame));
    }

    /**
     * inicia la actividad "panelAdminCultivos"
     * donde se muestran todos los cultivos
     * y las opciones para hacer el CRUD
     * @param view
     * tarjeta que genera el evento
     */
    public void abrirCultivos(View view){
        Intent intent = new Intent(PanelAdmin.this, PanelAdminCultivos.class);
        startActivity(intent);
    }

    /**
     * inicia la actividad donde se muestran
     * todos los usuarios:
     * @param view
     * btn que dispara el evento
     */
    public void abrirPanelUsuarios(View view){
        Intent intent = new Intent(PanelAdmin.this, PanelAdminUsuarios.class);
        startActivity(intent);
    }

    /**
     * inicia la actividad donde se muestran
     * todas las recetas:
     * @param view
     * btn que dispara el evento
     */
    public void abrirPanelRecetas(View view){
        Intent intent = new Intent(PanelAdmin.this, PanelAdminRecetas.class);
        startActivity(intent);
    }
}
package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * Primera pantalla del panel de admin
 * donde se selecciona si se quiere
 * administrar la coleccion de "cultivos o usuarios"
 */
public class PanelAdmin extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin);
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

    //TODO: agregar funcion que identifique si el user logueado es admin
    // recien alli, el user podra ir a la siguiente pantalla
    // de lo contrario se muestra una alerta
    // (quizas se podria restringir el acceso a esta pantalla directamente?)
}
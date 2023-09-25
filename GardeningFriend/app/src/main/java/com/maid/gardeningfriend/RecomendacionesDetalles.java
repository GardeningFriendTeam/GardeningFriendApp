package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * crea vista con detalles a partir de la
 * tarjeta seleccionada
 */
public class RecomendacionesDetalles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones_detalles);

        //elementos interfaz
        TextView tituloCultivo = findViewById(R.id.titulo_cultivo_detalles);
        TextView infoCultivo = findViewById(R.id.info_cultivo_detalles);

        // 1 - se reciben los valores de la pantalla anterior via intent
        String nombreCultivo = getIntent().getStringExtra("NOMBRE_CULTIVO");
        String infoCultivoValor = getIntent().getStringExtra("INFO_CULTIVO");

        // 2 - estos nuevos valores se pasan a la plantilla XML
        tituloCultivo.setText(nombreCultivo);
        infoCultivo.setText(infoCultivoValor);
    }

    public void btn_volver_reco(View view){
        // 1 - se crea el intent
        Intent intent = new Intent(this, Recomendaciones.class);
        // 2 - se redirige al nuevo intent
        startActivity(intent);
    }

    public void btn_volver_cultivos(View view){
        // se cierra la actividad actual mostrando la act previa
        finish();
    }
}
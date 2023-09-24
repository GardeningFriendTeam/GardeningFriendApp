package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

/**
 * segunda pantalla de "recomendaciones"
 * se muestran los cultivos filtrados
 */
public class RecomendacionesCultivos extends MainActivity {

    //variables que contienen los parametros selec
    String tempSelec;
    String estSelec;
    String regSelec;
    // array que contiene los cultivos que coinciden con los parametros


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones_cultivos);

        //se recibe el objeto parceable con los datos
        CultivosReco opcSeleccionadas = getIntent().getParcelableExtra("datosReco");

        // se reciben los datos selec en la pantalla anterior
        tempSelec = opcSeleccionadas.getTemperaturaSelec();
        estSelec = opcSeleccionadas.getEstacionSelec();
        regSelec = opcSeleccionadas.getRegSelec();

        //


    }
}
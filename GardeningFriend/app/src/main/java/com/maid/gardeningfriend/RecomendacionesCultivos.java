package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class RecomendacionesCultivos extends AppCompatActivity {

    String tempSelec;
    String estSelec;
    String regSelec;

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

        //se muestran los valores seleccionados
        Toast.makeText(this, tempSelec, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, estSelec, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, regSelec, Toast.LENGTH_SHORT).show();


    }
}
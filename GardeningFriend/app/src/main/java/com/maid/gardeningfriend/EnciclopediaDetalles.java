package com.maid.gardeningfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * crea vista con detalles a partir de la
 * tarjeta seleccionada
 */
public class EnciclopediaDetalles extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enciclopedia_detalles);

        //elementos interfaz
        TextView tituloCultivo = findViewById(R.id.titulo_cultivo_detalles);
        TextView infoCultivo = findViewById(R.id.info_cultivo_detalles);
        TextView tempCultuvo = findViewById(R.id.detalles_temperatura);
        TextView estCultivo = findViewById(R.id.detalles_estacion);
        TextView regCultivo = findViewById(R.id.detalles_region);
        ImageView imgCultivo = findViewById(R.id.ic_cultivo_detalles);

        // 1 - se reciben los valores de la pantalla anterior via intent
        // y se guardan en un objeto;
        CultivosDetallesParceable detallesCultivoSelec = getIntent().getParcelableExtra("CULTIVO_DETALLES");

        // 2 - estos nuevos valores se pasan a la plantilla XML
        tituloCultivo.setText(detallesCultivoSelec.getNombreCultivo());
        infoCultivo.setText(detallesCultivoSelec.getInfoCultivo());
        tempCultuvo.setText(detallesCultivoSelec.getTempCultivo());
        estCultivo.setText(detallesCultivoSelec.getEstCultivo());
        regCultivo.setText(detallesCultivoSelec.getRegCultivo());
        // se procesa imagen con 'picasso'
        Picasso.get()
                .load(detallesCultivoSelec.getImgCultivo())
                .error(R.mipmap.logo)
                .into(imgCultivo);
    }

    public void btn_volver_reco(View view){
        // 1 - se crea el intent
        Intent intent = new Intent(this, Enciclopedia.class);
        // 2 - se redirige al nuevo intent
        startActivity(intent);
    }

    public void btn_volver_cultivos(View view){
        // se cierra la actividad actual mostrando la act previa
        finish();
    }
}
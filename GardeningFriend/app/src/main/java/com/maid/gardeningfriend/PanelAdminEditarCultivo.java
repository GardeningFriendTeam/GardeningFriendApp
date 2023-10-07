package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PanelAdminEditarCultivo extends PanelAdminAgregarCultivo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_editar_cultivo);

        // se recibe el obj parseable de la pantalla anterior
        CultivosDetallesParceable cultivoSeleccionado = getIntent().getParcelableExtra("CULTIVO_SELEC_ADMIN");

        // se extraen sus propiedades y se las inserta en los campos de texto de la plantilla
        TextView tituloCultivo = findViewById(R.id.titulo_edit_cultivo);
        tituloCultivo.setText(cultivoSeleccionado.getNombreCultivo());

        TextView nombreCultivo = findViewById(R.id.nombre_edit_cultivo);
        nombreCultivo.setText(cultivoSeleccionado.getNombreCultivo());

        TextView infoCultivo = findViewById(R.id.info_edit_cultivo);
        infoCultivo.setText(cultivoSeleccionado.getInfoCultivo());

        TextView tipoCultivo = findViewById(R.id.tipo_edit_cultivo);
        tipoCultivo.setText(cultivoSeleccionado.getTipoCultivo());

        TextView tiempoCultivo = findViewById(R.id.tiempo_edit_cultivo);
        tiempoCultivo.setText(cultivoSeleccionado.getCrecimientoCultivo());

        TextView imagenCultivo = findViewById(R.id.imagen_edit_cultivo);
        imagenCultivo.setText(cultivoSeleccionado.getImgCultivo());

        TextView tempCultivo = findViewById(R.id.temp_edit_cultivo);
        tempCultivo.setText(cultivoSeleccionado.getTempCultivo());

        TextView estCultivo = findViewById(R.id.est_edit_cultivo);
        estCultivo.setText(cultivoSeleccionado.getEstCultivo());

        TextView regCultivo = findViewById(R.id.reg_edit_cultivo);
        regCultivo.setText(cultivoSeleccionado.getRegCultivo());

        ImageView iconoCultivo = findViewById(R.id.icono_cultivo_PA);
        // se utiliza la libreria "picasso" para procesar la img
        Picasso.get()
                .load(cultivoSeleccionado.getImgCultivo())
                .error(R.mipmap.logo)
                .into(iconoCultivo);

        //mensaje con instruccion de uso
        Toast msjInstruccion = Toast.makeText(PanelAdminEditarCultivo.this,
                "modifica los campos que necesites, aquellos que no quieras modificar dejalos en blanco",
                Toast.LENGTH_SHORT);

    }

    



}
package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Recomendaciones extends MainActivity {

    // variables que almacenan los valores seleccionados
    String temperatura;
    String estacion;
    String region;
    // variables flag que comprueban si una opcion fue selec
    boolean flagTemp = false;
    boolean flagEstac = false;
    boolean flagReg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones);
    }

    public void btnTempClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flagTemp = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.rango0) {
            btnSelec = findViewById(R.id.rango0);
            temperatura = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango1){
            btnSelec = findViewById(R.id.rango1);
            temperatura = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango2){
            btnSelec = findViewById(R.id.rango2);
            temperatura = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango3){
            btnSelec = findViewById(R.id.rango3);
            temperatura = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango4){
            btnSelec = findViewById(R.id.rango3);
            temperatura = btnSelec.getText().toString();

        } else {
            temperatura = "default";
        }

        // muestra la opc selec
        Toast avisoSelec = Toast.makeText(this, "seleccionaste: " + temperatura, Toast.LENGTH_SHORT);
        avisoSelec.show();
    }

    public void btnEstacClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flagEstac = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if(view.getId() == R.id.opc_verano){
            btnSelec = findViewById(R.id.opc_verano);
            estacion = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_otono){
            btnSelec = findViewById(R.id.opc_otono);
            estacion = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_invierno){
            btnSelec = findViewById(R.id.opc_invierno);
            estacion = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_primavera){
            btnSelec = findViewById(R.id.opc_primavera);
            estacion = btnSelec.getText().toString();

        } else {
            estacion = "default";
        }

        //muestra la opc seleccionada
        Toast avisoSelec = Toast.makeText(this, "seleccionaste: " + estacion, Toast.LENGTH_SHORT);
        avisoSelec.show();

    }

    public void btnRegClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flagReg = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.reg_norte){
            btnSelec = findViewById(R.id.reg_norte);
            region = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_centro){
            btnSelec = findViewById(R.id.reg_centro);
            region = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_cuyo){
            btnSelec = findViewById(R.id.reg_cuyo);
            region = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_patagonica){
            btnSelec = findViewById(R.id.reg_patagonica);
            region = btnSelec.getText().toString();

        } else {
            region = "default";
        }

        // este aviso muestra la seleccion del user
        Toast avisoSelec = Toast.makeText(this,"seleccionaste: " + region, Toast.LENGTH_SHORT);
        avisoSelec.show();
    }

    public void buscarCultivos(){
        // 1 - se crea intent
        Intent intent = new Intent(Recomendaciones.this, RecomendacionesCultivos.class);

        // toast que muestra mensaje de error (en caso de ocurrir):
        Toast toast = Toast.makeText(Recomendaciones.this, "no seleccionaste todos los parametros", Toast.LENGTH_SHORT);

        // TODO: 2 - se pasan los valores seleccionados a la siguiente actividad



    }



}
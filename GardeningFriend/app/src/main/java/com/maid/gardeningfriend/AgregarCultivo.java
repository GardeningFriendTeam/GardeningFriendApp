package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * Esta clase contiene todas las funcionalidades para
 * agregar cultivos desde el panel de admin
 */
public class AgregarCultivo extends MainActivity {

    // Atributos (valores extraidos de los edit texts)
    String inputNombre;
    String inputTipo;
    String inputInfo;
    String inputImg;
    String inputTemp;
    String inputEst;
    String inputReg;

    // cada elem representa un campo:
    boolean[] flags = new boolean[7];
    boolean validacionFinal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cultivo);

    }

    /**
     * ejecuta una serie de funciones para validar
     * los inputs y finalmente realizar una petincion
     * a la BD para añadir el doc nvo
     * @param view
     * btn que dispara el evento
     */
    public void agregarCultivoNvo(View view){
        //identificando elementos de la plantilla XML
        EditText campoNombre = findViewById(R.id.nombre_cultivo);
        EditText campoInfo = findViewById(R.id.info_cultivo);
        EditText campoTipo = findViewById(R.id.tipo_cultivo);
        EditText campoImg = findViewById(R.id.link_imagen);
        RadioGroup opcTemp = findViewById(R.id.radius_temp);
        RadioGroup opcEst = findViewById(R.id.radius_est);
        RadioGroup opcReg = findViewById(R.id.radius_region);

        validarTextoString(campoNombre,inputNombre, flags[0]);
        extraerTexto(campoInfo, inputInfo, flags[1]);
        validarTextoString(campoTipo, inputTipo, flags[2]);
        extraerTexto(campoImg, inputImg, flags[3]);

        validarCultivoNuevo();

    }


    /**
     * Esta funcion valida que el campo que se pase como argumento
     * solo contenga valores "string", para esto se usa una regex.
     * @param textField
     * se refiere EditText de la plantilla XML
     * @param inputString
     * se refiere a la variable que va a almacenar el valor
     * extraido del otro parametro
     */
    public void validarTextoString(EditText textField, String inputString, boolean flag){
        // mensaje de error
        Toast mensajeError = Toast.makeText(AgregarCultivo.this,
                "no se permiten numeros ni caracteres especiales!",
                Toast.LENGTH_SHORT);

        // se valida en base a una expresion regular
        if (textField.getText().toString().matches(".*\\d+.*")){
            mensajeError.show();
            flag = false;
        }else{
            inputString = textField.getText().toString();
            flag = true;
        }

    }

    /**
     * simplemente extrae el text de un editText
     * @param textField
     * editText
     * @param inputString
     * variable que almacenara el valor del param anterior
     */
    public void extraerTexto(EditText textField, String inputString, boolean flag){
        inputString = textField.getText().toString();
        if(!inputString.isEmpty()){
            // se valida que el campo no este vacio
            flag = true;
        } else{
            flag = false;
        }
    }

    /**
     * valida que btn fue seleccionado
     * en el radius group
     * @param view
     * btn que disparo el evento
     */
    public void btnTempClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flags[4] = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.rango0) {
            btnSelec = findViewById(R.id.rango0);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango1){
            btnSelec = findViewById(R.id.rango1);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango2){
            btnSelec = findViewById(R.id.rango2);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango3){
            btnSelec = findViewById(R.id.rango3);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.rango4){
            btnSelec = findViewById(R.id.rango3);
            inputTemp = btnSelec.getText().toString();

        } else {
            inputTemp = "default";
        }

    }

    /**
     * valida que btn fue seleccionado
     * en el radius group
     * @param view
     * btn que disparo el evento
     */
    public void btnEstacClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flags[5] = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if(view.getId() == R.id.opc_verano){
            btnSelec = findViewById(R.id.opc_verano);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_otono){
            btnSelec = findViewById(R.id.opc_otono);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_invierno){
            btnSelec = findViewById(R.id.opc_invierno);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc_primavera){
            btnSelec = findViewById(R.id.opc_primavera);
            inputEst = btnSelec.getText().toString();

        } else {
            inputEst = "default";
        }

    }

    /**
     * valida que btn fue seleccionado
     * en el radius group
     * @param view
     * btn que disparo el evento
     */
    public void btnRegClicked(View view){
        // var elem selec
        RadioButton btnSelec;
        // se verifica que un elem fue selec:
        flags[6] = ((RadioButton) view).isChecked();
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.reg_norte){
            btnSelec = findViewById(R.id.reg_norte);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_centro){
            btnSelec = findViewById(R.id.reg_centro);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_cuyo){
            btnSelec = findViewById(R.id.reg_cuyo);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.reg_patagonica){
            btnSelec = findViewById(R.id.reg_patagonica);
            inputReg = btnSelec.getText().toString();

        } else {
            inputReg = "default";
        }

    }

    /**
     * verifica que todos los inputs
     * brindados sean validos para llamar
     * a la funcion que sube el cultivo a la BD
     */
    public void validarCultivoNuevo(){
        // mensajes toasts
        Toast mensajeExito = Toast.makeText(AgregarCultivo.this,
                "inputs validos!",
                Toast.LENGTH_SHORT);

        Toast mensajeError = Toast.makeText(AgregarCultivo.this,
                "inputs invalidos! verifca tus datos",
                Toast.LENGTH_SHORT);

        // validacion flags
        for (boolean flag : flags){
            if(!flag){
                validacionFinal = false;
                break;
            }
        }

        // validacion final
        if(validacionFinal){
            mensajeExito.show();
        } else{
            mensajeError.show();
        }
    }



}
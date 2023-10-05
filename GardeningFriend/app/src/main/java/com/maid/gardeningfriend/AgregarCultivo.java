package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

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

    // representa una flag asociada a cada campo
    boolean[] flags = new boolean[7];
    boolean validacionFinal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cultivo);

    }



    /**
     * extrae el texto del editText que se pase como argumento
     * @param editText
     * campo xml
     * @return textoExtraido
     * valor string del campo
     */
    public String extraerTexto(EditText editText){
        String textoExtraido = editText.getText().toString();
        return textoExtraido;
    }

    /**
     * valida si el campo esta completo a no
     * @param inputText
     * string a validar
     * @return boolean
     * estado de la flag
     */
    public boolean validarInput(String inputText){
        if (!inputText.isEmpty()){
            // input valido
            return true;
        } else{
            // input invalido
            return false;
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
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.opc1_temp) {
            btnSelec = findViewById(R.id.opc1_temp);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc2_temp){
            btnSelec = findViewById(R.id.opc2_temp);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc3_temp){
            btnSelec = findViewById(R.id.opc3_temp);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc4_temp){
            btnSelec = findViewById(R.id.opc4_temp);
            inputTemp = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc5_temp){
            btnSelec = findViewById(R.id.opc5_temp);
            inputTemp = btnSelec.getText().toString();

        } else {
            inputTemp = "default";
        }

        flags[4] = validarInput(inputTemp);

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
        // se identifica que btn fue seleccionado
        if(view.getId() == R.id.opc1_est){
            btnSelec = findViewById(R.id.opc1_est);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc2_est){
            btnSelec = findViewById(R.id.opc2_est);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc3_est){
            btnSelec = findViewById(R.id.opc3_est);
            inputEst = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc4_est){
            btnSelec = findViewById(R.id.opc4_est);
            inputEst = btnSelec.getText().toString();

        } else {
            inputEst = "default";
        }

        flags[5] = validarInput(inputEst);

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
        // se identifica que btn fue seleccionado
        if (view.getId() == R.id.opc1_reg){
            btnSelec = findViewById(R.id.opc1_reg);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc2_reg){
            btnSelec = findViewById(R.id.opc2_reg);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc3_reg){
            btnSelec = findViewById(R.id.opc3_reg);
            inputReg = btnSelec.getText().toString();

        } else if (view.getId() == R.id.opc4_reg){
            btnSelec = findViewById(R.id.opc4_reg);
            inputReg = btnSelec.getText().toString();

        } else {
            inputReg = "default";
        }

        flags[6] = validarInput(inputReg);

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
        for (int i = 0; i < flags.length; i++) {
            if (!flags[i]){
                // error detectado
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

    /**
     * ejecuta una serie de funciones para validar
     * los edit texts finalmente realizar una petincion
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

        inputNombre = extraerTexto(campoNombre);
        flags[0] = validarInput(inputNombre);

        inputInfo = extraerTexto(campoInfo);
        flags[1] = validarInput(inputInfo);

        inputTipo = extraerTexto(campoTipo);
        flags[2] = validarInput(inputTipo);

        inputImg = extraerTexto(campoImg);
        flags[3] = validarInput(inputImg);

        Log.i("tag","edit texts agregados");
        validarCultivoNuevo();


    }




}
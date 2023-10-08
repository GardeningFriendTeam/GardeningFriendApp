package com.maid.gardeningfriend;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase contiene todas las funcionalidades para
 * agregar cultivos desde el panel de admin
 */
public class PanelAdminAgregarCultivo extends MainActivity {

    // Atributos (valores extraidos de los edit texts)
    private String inputNombre;
    private String inputTipo;
    private String inputInfo;
    private String inputCrecimiento;
    private String inputImg;
    private String inputTemp;
    private String inputEst;
    private String inputReg;

    // representa una flag asociada a cada campo
    private boolean[] flags = new boolean[8];
    private boolean validacionFinal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cultivo);

    }


    //TODO: AGREGAR OTRA FUNCION PARA VALIDAR EL NOMBRE / TIPO
    // YA QUE ESTOS CAMPOS NO DEBEN PERMITIR VALORES NUMERICOS O CARAC. ESPECIALES

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

        flags[5] = validarInput(inputTemp);

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

        flags[6] = validarInput(inputEst);

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

        flags[7] = validarInput(inputReg);

    }


    /**
     * verifica que todos los inputs
     * brindados sean validos para llamar
     * a la funcion que sube el cultivo a la BD
     */
    public void validarCultivoNuevo(){
        // mensajes toasts
        Toast mensajeExito = Toast.makeText(PanelAdminAgregarCultivo.this,
                "cultivo añadido!",
                Toast.LENGTH_SHORT);

        Toast mensajeError = Toast.makeText(PanelAdminAgregarCultivo.this,
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
            subirCultivoBD();
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
        EditText campoCrecimiento = findViewById(R.id.crecimiento_cultivo);

        inputNombre = extraerTexto(campoNombre);
        flags[0] = validarInput(inputNombre);

        inputInfo = extraerTexto(campoInfo);
        flags[1] = validarInput(inputInfo);

        inputTipo = extraerTexto(campoTipo);
        flags[2] = validarInput(inputTipo);

        inputImg = extraerTexto(campoImg);
        flags[3] = validarInput(inputImg);

        inputCrecimiento = extraerTexto(campoCrecimiento);
        flags[4] = validarInput(inputCrecimiento);

        Log.i("tag","edit texts agregados");
        validarCultivoNuevo();


    }

    /**
     * funcion para agregar cultivo a Firesore
     * como documento parte de la collecion "cultivos"
     */
    private void subirCultivoBD(){
        // 1 - se crea el objeto que se pasa como doc a la BD
        Map<String, Object> cultivoNvo = new HashMap<>();
        cultivoNvo.put("id", inputNombre);
        cultivoNvo.put("nombre", inputNombre);
        cultivoNvo.put("info", inputInfo);
        cultivoNvo.put("tipo", inputTipo);
        cultivoNvo.put("crecimiento", inputCrecimiento);
        cultivoNvo.put("icono", inputImg);
        cultivoNvo.put("temperatura", inputTemp);
        cultivoNvo.put("estacion", inputEst);
        cultivoNvo.put("region",inputReg);

        // 2 - se realiza una add request a la BD para agregar el doc
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cultivos")
                // add request
                .add(cultivoNvo)
                // listener (para corrobar si la request se dio exitosamente)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("tag", "documento agregado ID:" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tag", "error al cargar documento", e);
                    }
                });

    }




}
package com.maid.gardeningfriend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * representa un formulario que permite actualizar el cultivo selec
 */
public class PanelAdminEditarCultivo extends MainActivity {
    // Atributos

    // nombre
    private String IDcultivo;
    private String inputEditNombre;
    private EditText editTextNombre;
    // info
    private String inputEditInfo;
    private EditText editTextInfo;
    // tipo
    private String inputEditTipo;
    private EditText editTextTipo;
    // imagen
    private String inputEditImagen;
    private EditText editTextImg;
    // crecimiento
    private String inputEditCrecimiento;
    private EditText editTextCrecimiento;
    // temperatura
    private String inputEditTemp;
    private RadioButton RBtemp;
    // estacion
    private String inputEditEst;
    private RadioButton RBest;
    // region
    private String inputEditReg;
    private RadioButton RBreg;

    // flag final
    private boolean validacionFinalEdits = true;

    /**
     * se extraen las propiedades que se
     * pasaron via intent usando un parceable
     * @param savedInstanceState
     * parametro por defecto
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_editar_cultivo);

        // se recibe el obj parseable de la pantalla anterior
        CultivosDetallesParceable cultivoSeleccionado = getIntent().getParcelableExtra("CULTIVO_SELEC_ADMIN");

        // se extraen sus propiedades y se las inserta en los campos de texto de la plantilla
        TextView tituloCultivo = findViewById(R.id.titulo_edit_cultivo);
        tituloCultivo.setText(cultivoSeleccionado.getNombreCultivo());

        // ID
        IDcultivo = cultivoSeleccionado.getNombreCultivo();
        // nombre
        editTextNombre = findViewById(R.id.inputNombre_edit_cultivo);
        editTextNombre.setHint(cultivoSeleccionado.getNombreCultivo());
        inputEditNombre = cultivoSeleccionado.getNombreCultivo();
        // info
        editTextInfo = findViewById(R.id.inputInfo_edit_cultivo);
        editTextInfo.setHint(cultivoSeleccionado.getInfoCultivo());
        inputEditInfo = cultivoSeleccionado.getInfoCultivo();
        // tipo
        editTextTipo = findViewById(R.id.inputTipo_edit_cultivo);
        editTextTipo.setHint(cultivoSeleccionado.getTipoCultivo());
        inputEditTipo = cultivoSeleccionado.getTipoCultivo();
        // imagen
        editTextImg = findViewById(R.id.inputImagen_edit_cultivo);
        editTextImg.setHint(cultivoSeleccionado.getImgCultivo());
        inputEditImagen = cultivoSeleccionado.getImgCultivo();
        // tiempo crecimiento
        editTextCrecimiento = findViewById(R.id.inputTiempo_edit_cultivo);
        editTextCrecimiento.setHint(cultivoSeleccionado.crecimientoCultivo);
        inputEditCrecimiento = cultivoSeleccionado.getCrecimientoCultivo();
        // temperatura
        inputEditTemp = cultivoSeleccionado.getTempCultivo();
        String[] arrayTemp = getResources().getStringArray(R.array.array_temp);
        RadioButton[] opcionesTemp = new RadioButton[arrayTemp.length];
        opcionesTemp[0] = findViewById(R.id.opc1_temp_PAC);
        opcionesTemp[1] = findViewById(R.id.opc2_temp_PAC);
        opcionesTemp[2] = findViewById(R.id.opc3_temp_PAC);
        opcionesTemp[3] = findViewById(R.id.opc4_temp_PAC);
        opcionesTemp[4] = findViewById(R.id.opc5_temp_PAC);
        setDefaultRaudiusBtn(inputEditTemp, arrayTemp, opcionesTemp);
        // estacion
        inputEditEst = cultivoSeleccionado.getEstCultivo();
        String[] arrayEst = getResources().getStringArray(R.array.array_est);
        RadioButton[] opcionesEst = new RadioButton[arrayEst.length];
        opcionesEst[0] = findViewById(R.id.opc1_est_PAC);
        opcionesEst[1] = findViewById(R.id.opc2_est_PAC);
        opcionesEst[2] = findViewById(R.id.opc3_est_PAC);
        opcionesEst[3] = findViewById(R.id.opc4_est_PAC);
        setDefaultRaudiusBtn(inputEditEst, arrayEst, opcionesEst);
        // region
        inputEditReg = cultivoSeleccionado.getRegCultivo();
        String[] arrayReg = getResources().getStringArray(R.array.array_reg);
        RadioButton[] opcionesReg = new RadioButton[arrayReg.length];
        opcionesReg[0] = findViewById(R.id.opc1_reg_PAC);
        opcionesReg[1] = findViewById(R.id.opc2_reg_PAC);
        opcionesReg[2] = findViewById(R.id.opc3_reg_PAC);
        opcionesReg[3] = findViewById(R.id.opc4_reg_PAC);
        setDefaultRaudiusBtn(inputEditReg, arrayReg, opcionesReg);


        ImageView iconoCultivo = findViewById(R.id.icono_cultivo_PA);
        inputEditImagen = cultivoSeleccionado.getImgCultivo();

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


    /**
     * identifica que btnRadius corresponde
     * a la opcion del cultivo
     * @param inputSelec
     * valor string
     * @param valoresString
     * array con opciones string
     * @param radiusBtns
     * array de botones
     */
    public void setDefaultRaudiusBtn(String inputSelec, String[] valoresString, RadioButton[] radiusBtns){
        for (int i = 0; i < radiusBtns.length; i++) {
            if (inputSelec.equals(valoresString[i])){
                radiusBtns[i].setChecked(true);
            }
        }
    }


    /**
     * valida que el texto no incluya
     * numeros ni caracteres especiales
     * @param editText
     * campo de texto
     */
    public String validarTexto(EditText editText){
        Pattern pattern = Pattern.compile("^a-zA-ZáéíóúñÁÉÍÓÚÑ");
        String input = editText.getText().toString();
        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.matches();

        // se valida el regex
        // true = error / false = el texto esta ok
        if(!matchFound){
            return input;
        } else{
            return "error";
        }
    }

    /**
     * simplemente valida que el campo este completo
     * @param editText
     * campo de texto
     * @return
     * texto
     */
    public String extraerTexto(EditText editText){
        String input = editText.getText().toString();
        if(!input.isEmpty()){
            return input;
        } else {
            return "error";
        }
    }

    /**
     * cuando se clickea cualquier btn radius
     * se identifica cual fue
     * @param view
     * btn que dispara el evento
     */
    public void extraerTempSelec(View view){
        if(view.getId() == R.id.opc1_temp_PAC){
            RBtemp = findViewById(R.id.opc1_temp_PAC);
            inputEditTemp = RBtemp.getText().toString();

        } else if(view.getId() == R.id.opc2_temp_PAC){
            RBtemp = findViewById(R.id.opc2_temp_PAC);
            inputEditTemp = RBtemp.getText().toString();

        } else if(view.getId() == R.id.opc3_temp_PAC) {
            RBtemp = findViewById(R.id.opc3_temp_PAC);
            inputEditTemp = RBtemp.getText().toString();

        } else if (view.getId() == R.id.opc4_temp_PAC) {
            RBtemp = findViewById(R.id.opc4_temp_PAC);
            inputEditTemp = RBtemp.getText().toString();

        } else if(view.getId() == R.id.opc5_temp_PAC){
            RBtemp = findViewById(R.id.opc5_temp_PAC);
            inputEditTemp = RBtemp.getText().toString();

        } else{
            inputEditTemp = "error";
        }
    }

    /**
     * cuando se clickea cualquier btn radius
     * se identifica cual fue
     * @param view
     * btn que dispara el evento
     */
    public void extraerEstSelec(View view){
        if(view.getId() == R.id.opc1_est_PAC){
            RBest = findViewById(R.id.opc1_est_PAC);
            inputEditEst = RBest.getText().toString();

        } else if(view.getId() == R.id.opc2_est_PAC){
            RBest = findViewById(R.id.opc1_est_PAC);
            inputEditEst = RBest.getText().toString();

        } else if(view.getId() == R.id.opc3_est_PAC) {
            RBest = findViewById(R.id.opc3_est_PAC);
            inputEditEst = RBest.getText().toString();

        } else if (view.getId() == R.id.opc4_est_PAC) {
            RBest = findViewById(R.id.opc4_est_PAC);
            inputEditEst = RBest.getText().toString();

        }  else{
            inputEditEst = "error";
        }
    }

    /**
     * cuando se clickea cualquier btn radius
     * se identifica cual fue
     * @param view
     * btn que dispara el evento
     */
    public void extraerRegSelec(View view){
        if(view.getId() == R.id.opc1_reg_PAC){
            RBreg = findViewById(R.id.opc1_reg_PAC);
            inputEditReg = RBreg.getText().toString();

        } else if(view.getId() == R.id.opc2_reg_PAC){
            RBreg = findViewById(R.id.opc1_reg_PAC);
            inputEditEst = RBreg.getText().toString();

        } else if(view.getId() == R.id.opc3_reg_PAC) {
            RBreg = findViewById(R.id.opc3_reg_PAC);
            inputEditEst = RBreg.getText().toString();

        } else if (view.getId() == R.id.opc4_reg_PAC) {
            RBreg = findViewById(R.id.opc4_reg_PAC);
            inputEditEst = RBreg.getText().toString();

        }  else{
            inputEditReg = "error";
        }
    }


    /**
     * valida que los campos se hayan completado
     * correctamente
     * @param view
     * boton que dispara el evento
     */
    public void validarFormulario(View view){

        // se extraen los valores de cada editText
        inputEditNombre = validarTexto(editTextNombre);
        inputEditInfo = extraerTexto(editTextInfo);
        inputEditTipo = validarTexto(editTextTipo);
        inputEditCrecimiento = extraerTexto(editTextCrecimiento);
        inputEditImagen = extraerTexto(editTextImg);

        // los radius buttons se identifican en otros metodos aparte

        //mensajes de alerta
        Toast msjExito = Toast.makeText(PanelAdminEditarCultivo.this,
                "operacion valida!",
                Toast.LENGTH_SHORT );

        Toast msjError = Toast.makeText(PanelAdminEditarCultivo.this,
                "inputs invalidos, recuerda que 'nombre' y 'tipo' solo aceptan carac. alfabeticos",
                Toast.LENGTH_SHORT);


        // contiene una flag que se refiere a cada elem
        boolean[] flags = new boolean[8];
        // valor string extraido de cada campo
        String[] atributos = new String[8];
        atributos[0] = inputEditNombre;
        atributos[1] = inputEditInfo;
        atributos[2] = inputEditTipo;
        atributos[3] = inputEditImagen;
        atributos[4] = inputEditCrecimiento;
        atributos[5] = inputEditTemp;
        atributos[6] = inputEditEst;
        atributos[7] = inputEditReg;

        // a traves de un loop se validan los inputs
        // se asigna una flag a cada atributo
        for (int i = 0; i < atributos.length; i++) {
            if(atributos[i].equals("error") || atributos[i].isEmpty()){
                //input no valido
                flags[i] = false;

            } else{
                //input valido
                flags[i] = true;
            }
        }

        // se controla el estado de cada flag individual
        // si hay un error se muestra la alerta
        for (boolean flag : flags) {
            if(!flag){
                validacionFinalEdits = false;
                msjError.show();
            }
        }

        //se muestra un msj si todos los imputs son validos
        if(validacionFinalEdits){
            msjExito.show();
            actualizarCultivo(IDcultivo);
        }

    }

    /**
     * una vez completada la validacion se actualiza el cultivo
     */
    private void actualizarCultivo(String IDcultivo){
        // mensajes de alerta
        Toast msjExito = Toast.makeText(PanelAdminEditarCultivo.this,
                "cultivo actualizado",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(PanelAdminEditarCultivo.this,
                "no se ha podido actualizar el cultivo",
                Toast.LENGTH_SHORT);

        // 1 - se crea una instancia de la BD / coleccion a utilizar
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("cultivos").document(IDcultivo);

        // 2 - se crea un objeto con los datos actualizados del cultivo
        Map<String, Object> data = new HashMap<>();
        data.put("id", IDcultivo);
        data.put("nombre", inputEditNombre);
        data.put("informacion", inputEditInfo);
        data.put("duracion", inputEditCrecimiento);
        data.put("tipo", inputEditTipo);
        data.put("icono", inputEditImagen);
        data.put("temperatura", inputEditTemp);
        data.put("estacion", inputEditEst);
        data.put("region", inputEditReg);

        // 3 - se realiza una peticion "update" al documento indicado pasando el objeto como param
        docRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                msjExito.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                msjError.show();
                Log.w("tag", "ha ocurrido un error al conectar con la BD: " + e);
            }
        });

    }

}
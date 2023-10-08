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

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * representa un formulario que permite actualizar el cultivo selec
 */
public class PanelAdminEditarCultivo extends MainActivity {
    // Atributos

    // nombre
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
    private RadioGroup radioGroupTemp;
    private RadioButton RBtemp;
    // estacion
    private String inputEditEst;
    private RadioGroup radioGroupEst;
    private RadioButton RBest;
    // region
    private String inputEditReg;
    private RadioGroup radioGroupReg;
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





}
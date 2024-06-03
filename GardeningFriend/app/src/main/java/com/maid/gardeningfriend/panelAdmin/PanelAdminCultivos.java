package com.maid.gardeningfriend.panelAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.recomendaciones.CultivosGenerador;

import java.util.ArrayList;
import java.util.Map;

/**
 * Aca se muestran todos los cultivos que estan en la BD
 * para poder modificarlos / eliminarlos / agregar mas
 */
public class PanelAdminCultivos extends MainActivity implements PanelAdminInterfaceCultivos {
    //array que contiene todos los cultivos
    ArrayList<CultivosGenerador> cultivosBD = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_cultivos);

        // se activa la funcion que realiza el get request a la BD
        getCultivosBD();

    }

    /**
     * realiza una GET REQUEST a la bd
     * y consume todos los cultivos
     * guardandolos en "cultivosBD"
     */
    private void getCultivosBD(){
        //se obtiene instancia de la BD
        FirebaseFirestore bd = FirebaseFirestore.getInstance();
        // se realiza peticion
        bd.collection("cultivos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                // se convierte el doc iterado en un objeto
                                Map<String, Object> data = document.getData();

                                // se extraen propiedades para crear objeto
                                String nombre = (String) data.get("nombre");
                                String temperatura = (String) data.get("temperatura");
                                String estacion = (String) data.get("estacion");
                                String region = (String) data.get("region");
                                String info = (String) data.get("informacion");
                                String icono = (String) data.get("icono");
                                String tipo = (String) data.get("tipo");
                                String crecimiento = (String) data.get("crecimiento");

                                // se crea un nuevo objeto para ser agregado al arraylist
                                CultivosGenerador cultivoIterado = new CultivosGenerador(
                                        nombre,
                                        nombre,
                                        tipo,
                                        crecimiento,
                                        info,
                                        temperatura,
                                        estacion,
                                        region,
                                        icono
                                );

                                // se llama a funcion para que agregue el cultivo a al arraylist
                                agregarCultivo(cultivoIterado);
                            }
                            // se activa el adapter una vez que se itero sobre todos los docs
                            activarAdapter();

                        }else{
                            // se muestra mensaje de error en caso que falle la conexion
                            Toast.makeText(PanelAdminCultivos.this, "ha ocurrido un problema al conectar con la BD", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    /**
     * agrega cultivo al arraylist cultivosBD
     * (esto se hace a aparte porque la funcion que hace la peticion
     * es asincrinica)
     * @param cultivo
     * instacia de CultivosGenerador
     */
    public void agregarCultivo(CultivosGenerador cultivo){
        cultivosBD.add(cultivo);
    }

    /**
     * activa el adapter, esto se realiza aparte
     * debido a que la funcion que hace la GET REQUEST
     * es asincronica
     */
    public void activarAdapter(){
        //se identifica el recycler view
        RecyclerView recyclerAdmin = findViewById(R.id.recycler_panel_cultivos);
        // se crea el adapter para recyclerview
        PanelAdminCultivosRecyclerView adapter = new PanelAdminCultivosRecyclerView(this,cultivosBD,this);
        recyclerAdmin.setAdapter(adapter);
        recyclerAdmin.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * inicia la actividad para agregar un nuevo cultivo
     * @param view
     * vista que dispara la funcion
     */
    public void btnAgregarNuevo(View view){
        Intent intent = new Intent(PanelAdminCultivos.this, PanelAdminAgregarCultivo.class);
        startActivity(intent);
    }

    /**
     * identifica que tarjeta fue clickeada y llama a otra funcion
     * para eliminar el cultivo
     * @param position
     * index que representa la posicion
     */
    @Override
    public void eliminarBtn(int position) {
        String nombreCultivo = cultivosBD.get(position).getNombre();
        popUpEliminar(nombreCultivo);
    }

    /**
     * activa un popup para confirmar la operacion
     * @param nombreCultivo
     */
    public void popUpEliminar(String nombreCultivo){
        // se crea un pop up para confirmar la operacion
        PopupWindow popupWindow = new PopupWindow(this);
        // se identifica el contenedor padre
        RelativeLayout parentElem = findViewById(R.id.container_admin_cultivos);
        //efectos para animacion:
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        anim.setDuration(200);
        // se infla el elemento
        View popUpView = getLayoutInflater().inflate(R.layout.popup_panel_admin, null);
        popupWindow.setContentView(popUpView);
        popupWindow.getContentView().startAnimation(anim);
        // se especifica su tamaño (ocupa toda la pantalla)
        popupWindow.setHeight(500);
        popupWindow.setWidth(500);
        // se especifica la ubicacion del elem:
        popupWindow.showAtLocation(parentElem, Gravity.CENTER, 0, 0);

        //se identifican los botones del pop up:
        AppCompatButton btnEliminar = popUpView.findViewById(R.id.btn_eliminar_popup);
        AppCompatButton btnCancelar =  popUpView.findViewById(R.id.btn_cancelar_popup);

        //se programan los eventos:
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarCultivo(nombreCultivo);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // se cierra el popup
                popupWindow.dismiss();
            }
        });

    }

    /**
     * Realiza una peticion a la BD para eliminar el documento
     * @param nombreCultivo
     * ID del cultivo que se selecciono
     */
    public void eliminarCultivo(String nombreCultivo){
        // mensajes de alerta
        Toast msjExito = Toast.makeText(PanelAdminCultivos.this,
                "el cultivo ha sido eliminado de la BD",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(PanelAdminCultivos.this,
                "ha ocurrido un error al intentar eliminar el cultivo",
                Toast.LENGTH_SHORT);

        // se crea una instancia de la BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("cultivos").document(nombreCultivo);

        // se realiza una DELETE request sobre el documento seleccionado
        docRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            msjExito.show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        msjError.show();
                        Log.w("cultivodel", "error conexion BD", e);
                    }
                });
    }

    /**
     * identifica que tarjeta fue seleccionada y llama a otra funcion para
     * editar el cultivo
     * @param position
     * index que representa la posicion
     */
    @Override
    public void editarBtn(int position) {
        String IDcultivoSelec = cultivosBD.get(position).getID();
        editarCultivo(position);
    }

    /**
     * genera un intent para abrir otra pantalla
     * con las opciones de edicion
     * @param position
     * cultivo selecionado
     */
    public void editarCultivo(int position){
        // 1 - se crea un objeto parseable del obj selecionado
        CultivosDetallesParceable cultivoSelec =  new CultivosDetallesParceable(
                cultivosBD.get(position).getNombre(),
                cultivosBD.get(position).getTemperatura(),
                cultivosBD.get(position).getEstacionSiembra(),
                cultivosBD.get(position).getRegion(),
                cultivosBD.get(position).getCaracteristicas(),
                cultivosBD.get(position).getImagen(),
                cultivosBD.get(position).getTipo(),
                cultivosBD.get(position).getDuracionCrecimiento()
        );

        // 2 - se crea el intent y se pasa el objeto parseable
        Intent intent = new Intent(PanelAdminCultivos.this, PanelAdminEditarCultivo.class);
        intent.putExtra("CULTIVO_SELEC_ADMIN", cultivoSelec);

        // 3 - finalmente se inicia la actividad
        startActivity(intent);
    }



}



package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Aca se muestran todos los cultivos que estan en la BD
 * para poder modificarlos / eliminarlos / agregar mas
 */
public class PanelAdminCultivos extends MainActivity {

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
        PanelAdminRecyclerView adapter = new PanelAdminRecyclerView(this,cultivosBD);
        recyclerAdmin.setAdapter(adapter);
        recyclerAdmin.setLayoutManager(new LinearLayoutManager(this));
    }

}
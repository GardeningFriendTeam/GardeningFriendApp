package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Map;

/**
 * segunda pantalla de "recomendaciones"
 * se muestran los cultivos filtrados
 */
public class RecomendacionesCultivos extends MainActivity implements RecomendacionesInterface{

    //variables que contienen los parametros selec
    String tempSelec;
    String estSelec;
    String regSelec;
    // array que contiene los cultivos que coinciden con los parametros
    ArrayList<CultivosGenerador> cultivosFiltrados = new ArrayList<>();
    int[] imagenesCultivos = {R.mipmap.ic_aceituna, R.mipmap.ic_calabaza, R.mipmap.ic_cebolla, R.mipmap.ic_lechuga, R.mipmap.logo};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones_cultivos);

        //se recibe el objeto parceable con los datos
        CultivosReco opcSeleccionadas = getIntent().getParcelableExtra("datosReco");

        // se reciben los datos selec en la pantalla anterior
        tempSelec = opcSeleccionadas.getTemperaturaSelec();
        estSelec = opcSeleccionadas.getEstacionSelec();
        regSelec = opcSeleccionadas.getRegSelec();

        //se identifica el recyclerview de la activity
        RecyclerView recycler = findViewById(R.id.recycler_cultivos);

        //se inicializa funcion para agregar las tarjetas
        addModelsCultivos();

        // se activa el "adapter" para que pase las tarjetas al recycler
        RecomendacionesRecyclerView adapter = new RecomendacionesRecyclerView(this,cultivosFiltrados,this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * agrega los cultivos que coinciden con los param selecionados por el user
     * @return cultivos que se añaden a un array
     */
    private void addModelsCultivos(){
        // 1 - se crea una instancia de la BD para acceder a la coleccion
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //prueba valores seleccionados
        Log.i("tag", tempSelec);
        Log.i("tag", estSelec);
        Log.i("tag", regSelec);

        // 2 - se realiza una get request para consumir los datos de los cultivos
        db.collection("cultivos")
                .get()
                //listener que verifica si la peticion fue exitosa
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("tag","peticion exitosa");
                            // se recorre la coleccion y se extraen los datos necesarios
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // se guarda la info consumida en un map
                                Map<String, Object> data = document.getData();

                                // se accede a las propiedades del elem iterado
                                String nombre = (String) data.get("nombre");
                                String temperatura = (String) data.get("temperatura");
                                String estacion = (String) data.get("estacion");
                                String region = (String) data.get("region");
                                String informacion = (String) data.get("informacion");
                                String crecimiento = (String) data.get("crecimiento");
                                String tipo = (String) data.get("tipo");

                                Log.i("tag", nombre);
                                Log.i("tag", temperatura);
                                Log.i("tag", estacion);
                                Log.i("tag", region);

                                // se compara con la seleccion del usuario
                                if(tempSelec.equals(temperatura) &&
                                estSelec.equals(estacion)&&
                                regSelec.equals(region)){
                                    // se agrega al array que va a ser usado x recyclerview
                                    cultivosFiltrados.add(new CultivosGenerador(nombre,
                                            nombre,
                                            tipo,
                                            crecimiento,
                                            informacion,
                                            temperatura,
                                            estacion,
                                            region,
                                            imagenesCultivos[4]));
                                    Log.i("tag", "cultivo agregado");
                                }

                            }
                        } else {
                            // fracaso la peticion & se muestra mensaje
                            Toast.makeText(RecomendacionesCultivos.this,"hubo un error al conectarse con la BD", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        // 3 - en caso de no haber resultados se muestra un mensaje de error:
        if(cultivosFiltrados.size() == 0){
            Toast.makeText(RecomendacionesCultivos.this, "no se han encontrado resultados", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * pasa los valores de la tarjeta seleccionada
     * a la siguiente vista donde se muestran los detalles
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        // 1 - se crea intent para dirigir al user a la pantalla con info extra:
        Intent intent = new Intent(RecomendacionesCultivos.this, RecomendacionesDetalles.class);

        // 2 - se pasan las propiedades necesarias:
        intent.putExtra("NOMBRE_CULTIVO", cultivosFiltrados.get(position).getNombre());
        intent.putExtra("INFO_CULTIVO", cultivosFiltrados.get(position).getCaracteristicas());

        // 3 - se inicialza la nueva actividad (intent)
        startActivity(intent);
    }


}
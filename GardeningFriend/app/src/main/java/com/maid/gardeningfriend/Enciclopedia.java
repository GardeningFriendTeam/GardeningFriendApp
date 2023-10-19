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

import java.util.ArrayList;
import java.util.Map;

/*
* Pantalla principal de la enciclopedia
* */
public class Enciclopedia extends MainActivity implements EnciclopediaInterface{
    ArrayList<CultivosGenerador> cultivos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enciclopedia);

        addModelsCultivos();
    }

    /*
    * Agrega los cultivos
    * */
    private void addModelsCultivos() {
        // 1- se crea una instanccia de la BD para acceder a la colección
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2- se realiza un get request para consumir los datos de los cultivos
        db.collection("cultivos").get()
                // verifica si la petición fue exitosa
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("tag","peticion exitosa");
                            // se recorre la coleccion y se extraen los datos necesarios
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // se guarda la info consumida en un objeto map
                                Map<String, Object> data = document.getData();

                                // se extraen las propiedades del elem iterado
                                String nombre = (String) data.get("nombre");
                                String temperatura = (String) data.get("temperatura");
                                String estacion = (String) data.get("estacion");
                                String region = (String) data.get("region");
                                String informacion = (String) data.get("informacion");
                                String crecimiento = (String) data.get("crecimiento");
                                String tipo = (String) data.get("tipo");
                                String imagen = (String) data.get("icono");

                                // se crea objeto para agregar cultivo al array
                                CultivosGenerador nuevoCultivo = new CultivosGenerador(
                                        nombre,
                                        nombre,
                                        tipo,
                                        crecimiento,
                                        informacion,
                                        temperatura,
                                        estacion,
                                        region,
                                        imagen);

                                agregarCultivo(nuevoCultivo);

                            }
                            // una vez que se iteran todos los documentos se activa el recyclerview adapter
                            activarAdapter();

                            Log.i("tag", "tamaño array: " + cultivos.size());
                        } else {
                            // fracaso la peticion & se muestra mensajes
                            Toast.makeText(Enciclopedia.this,"hubo un error al conectarse con la BD", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void agregarCultivo(CultivosGenerador cultivo) {
        cultivos.add(cultivo);
        Log.i("tag", "cultivo agreagado: " + cultivo.nombre);
    }
    /**
     * activa el adapter de recycler view
     * para pasar todos los cultivos
     * guardados en el array "cultivosFiltrados"
     */
    public void activarAdapter(){
        //se identifica el recyclerview de la activity
        RecyclerView rv = findViewById(R.id.rvcultivos);

        // se activa el "adapter" para que pase las tarjetas al recycler
        EnciclopediaRecyclerView adapter = new EnciclopediaRecyclerView(this,cultivos,this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        // 1 - se crea intent para dirigir al user a la pantalla con info extra:
        Intent intent = new Intent(Enciclopedia.this, RecomendacionesDetalles.class);

        // 2 - se crea objeto parceable para pasar las propiedades del cult selec a la siguiente
        //pantalla
        CultivosDetallesParceable detallesCultivo = new CultivosDetallesParceable(
                cultivos.get(position).getNombre(),
                cultivos.get(position).getTemperatura(),
                cultivos.get(position).getEstacionSiembra(),
                cultivos.get(position).getRegion(),
                cultivos.get(position).getCaracteristicas(),
                cultivos.get(position).getImagen(),
                cultivos.get(position).getTipo(),
                cultivos.get(position).getDuracionCrecimiento()
        );

    }

}
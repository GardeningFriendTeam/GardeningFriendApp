package com.maid.gardeningfriend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * segunda pantalla de "enciclopedia"
 * se muestran los cultivos filtrados
 */
public class EnciclopediaCultivos extends MainActivity implements EnciclopediaInterface{
    EditText etBuscador;
    //variables que contienen los parametros selec
    String nombre;

    // array que contiene los cultivos que coinciden con los parametros
    ArrayList<CultivosGenerador> cultivosFiltrados = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enciclopedia_cultivos);


        etBuscador = findViewById(R.id.etBuscador);

        nombre = String.valueOf(etBuscador.getText());


        //se inicializa funcion para agregar las tarjetas
        addModelsCultivos();

    }

    /**
     * agrega los cultivos que coinciden con los param selecionados por el user
     */
    private void addModelsCultivos(){
        // 1 - se crea una instancia de la BD para acceder a la coleccion
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2 - se realiza una get request para consumir los datos de los cultivos
        db.collection("cultivos")
                // queries para filtrar el cultivo de acuerdo
                // a la búsqueda del user
                .whereEqualTo("nombre", nombre)
                .get()
                //listener que verifica si la peticion fue exitosa
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

                            Log.i("tag", "tamaño array: " + cultivosFiltrados.size());
                        } else {
                            // fracaso la peticion & se muestra mensaje
                            Toast.makeText(EnciclopediaCultivos.this,"hubo un error al conectarse con la BD", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * debido a que la get request es una
     * funcion asincronica el objeto de cada cultivo
     * filtrado se añade aparte
     * @param cultivoSelec
     * parametro representa el cultivo validado por el filtro
     */
    public void agregarCultivo(CultivosGenerador cultivoSelec){
        cultivosFiltrados.add(cultivoSelec);
        Log.i("tag", "cultivo agreagado: " + cultivoSelec.nombre);
    }

    /**
     * activa el adapter de recycler view
     * para pasar todos los cultivos
     * guardados en el array "cultivosFiltrados"
     */
    public void activarAdapter(){
        //se identifica el recyclerview de la activity
        RecyclerView recycler = findViewById(R.id.rvcultivos);

        // se activa el "adapter" para que pase las tarjetas al recycler
        EnciclopediaRecyclerView adapter = new EnciclopediaRecyclerView(this, cultivosFiltrados,this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * pasa los valores de la tarjeta seleccionada
     * a la siguiente vista donde se muestran los detalles
     * @param position
     * representa el index de la tarjeta
     */
    @Override
    public void onItemClick(int position) {
        // 1 - se crea intent para dirigir al user a la pantalla con info extra:
        Intent intent = new Intent(EnciclopediaCultivos.this, EnciclopediaDetalles.class);

        // 2 - se crea objeto parceable para pasar las propiedades del cult selec a la siguiente
        //pantalla
        CultivosDetallesParceable detallesCultivo = new CultivosDetallesParceable(
                cultivosFiltrados.get(position).getNombre(),
                cultivosFiltrados.get(position).getTemperatura(),
                cultivosFiltrados.get(position).getEstacionSiembra(),
                cultivosFiltrados.get(position).getRegion(),
                cultivosFiltrados.get(position).getCaracteristicas(),
                cultivosFiltrados.get(position).getImagen(),
                cultivosFiltrados.get(position).getTipo(),
                cultivosFiltrados.get(position).getDuracionCrecimiento()
        );

        //prueba
        Log.i("tag", "nombre cultivo: " + detallesCultivo.getNombreCultivo());

        // 3 - se pasa el objeto parceable al intent
        intent.putExtra("CULTIVO_DETALLES", detallesCultivo);

        // 4  - se inicialza la nueva actividad (intent)
        startActivity(intent);
    }




}
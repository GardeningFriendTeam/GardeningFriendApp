package com.maid.gardeningfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maid.gardeningfriend.favoritos.FavoritosSeccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Enciclopedia extends MainActivity implements EnciclopediaInterface{
    //Atributos principales
    SearchView searchView;
    RecyclerView recyclerView;
    EnciclopediaRecyclerViewAdapter adapter;
    ArrayList<CultivosGenerador> cultivosBD = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enciclopedia);

        getCultivosEnc();

        // se activa funcionalidad del buscador
        searchView = findViewById(R.id.buscador_enc);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String palabra) {
                barraBusquedaEnc(palabra);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cultivosBD.clear();
                getCultivosEnc();
                return false;
            }
        });

    }

    /**
     * realiza una get request a la BD
     * para extraer todos los cultivos
     */
    private void getCultivosEnc(){
        //instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //mensajes de alerta
        Toast msjExito = Toast.makeText(Enciclopedia.this,
                "conexion exitosa a la BD",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(Enciclopedia.this,
                "ocurrio un error al conectar con la BD",
                Toast.LENGTH_SHORT);

        //se realiza peticion
        db.collection("cultivos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            // se itera sobre cada doc de la coleccion
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // se convierta cada documento en un haspmap para poder manipularlo
                                Map<String, Object> cultivoDocument = document.getData();

                                String ID = (String) document.get("id");
                                String nombre = (String) document.get("nombre");
                                String tipo = (String) document.get("tipo");
                                String info = (String) document.get("informacion");
                                String crecimiento = (String) document.get("crecimiento");
                                String icono = (String) document.get("icono");
                                String temperatura = (String) document.get("temperatura");
                                String estacion = (String) document.get("estacion");
                                String region = (String) document.get("region");

                                //se crea un objeto de tipo "cultivo"
                                CultivosGenerador cultivo = new CultivosGenerador(
                                        ID,
                                        nombre,
                                        tipo,
                                        crecimiento,
                                        info,
                                        temperatura,
                                        estacion,
                                        region,
                                        icono
                                );
                                addCultivo(cultivo);
                            }
                            setAdapter();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * agrega el cultivo al array
     * que luego se pasa al adapter
     * @param cultivoExtraido
     * cultivo extraido de la BD
     */
    private void addCultivo(CultivosGenerador cultivoExtraido){
        cultivosBD.add(cultivoExtraido);
        Log.i("enc", "cultivo añadido: " + cultivoExtraido.getNombre());
    }

    /**
     * activa el adaptador y pasa el array de refe
     */
    private void setAdapter(){
        // se identifica el recyclerview
        recyclerView = findViewById(R.id.enc_recyclerview);
        // se activa su adaptador
        adapter = new EnciclopediaRecyclerViewAdapter(this, cultivosBD, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        // 1 - se crea intent para dirigir al user a la pantalla con info extra:
        Intent intent = new Intent(Enciclopedia.this, RecomendacionesDetalles.class);

        // 2 - se crea objeto parceable para pasar las propiedades del cult selec a la siguiente
        //pantalla
        CultivosDetallesParceable detallesCultivo = new CultivosDetallesParceable(
                cultivosBD.get(position).getNombre(),
                cultivosBD.get(position).getTemperatura(),
                cultivosBD.get(position).getEstacionSiembra(),
                cultivosBD.get(position).getRegion(),
                cultivosBD.get(position).getCaracteristicas(),
                cultivosBD.get(position).getImagen(),
                cultivosBD.get(position).getTipo(),
                cultivosBD.get(position).getDuracionCrecimiento()
        );

        //prueba
        Log.i("tag", "nombre cultivo: " + detallesCultivo.getNombreCultivo());

        // 3 - se pasa el objeto parceable al intent
        intent.putExtra("CULTIVO_DETALLES", detallesCultivo);

        // 4  - se inicialza la nueva actividad (intent)
        startActivity(intent);
    }

    private void barraBusquedaEnc(String input){
        //mensajes de alerta
        Toast msjExito = Toast.makeText(Enciclopedia.this,
                "resultados de busqueda: ",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(Enciclopedia.this,
                "no se encontraron resultados",
                Toast.LENGTH_SHORT);

        Toast errorBD = Toast.makeText(Enciclopedia.this,
                "error al conectar con la BD",
                Toast.LENGTH_SHORT);

        // 1 - se crea instancia de BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2 - se valida que la palabra este en favoritos
        Log.i("arrayfav", cultivosBD.toString());

        boolean flagInput = false;
        for (CultivosGenerador cultivo : cultivosBD){
            if(cultivo.getNombre().equals(input.toLowerCase())){
                flagInput = true;
            }
        }

        // 3 - se realiza la get request si la flag esta activada
        if(flagInput){
            // se limpia el array del adapter primero
            cultivosBD.clear();

            // se realiza otra get request para actualizar el array del adapter
            db.collection("cultivos")
                    .whereEqualTo("nombre", input)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult() ) {
                                    // se extrae cada objeto iterado en un hashmap
                                    Map<String, Object> cultivoDoc = documentSnapshot.getData();

                                    String ID = (String) cultivoDoc.get("id");
                                    String nombre = (String) cultivoDoc.get("nombre");
                                    String tipo = (String) cultivoDoc.get("tipo");
                                    String info = (String) cultivoDoc.get("informacion");
                                    String icono = (String) cultivoDoc.get("icono");
                                    String temperatura = (String) cultivoDoc.get("temperatura");
                                    String estacion = (String) cultivoDoc.get("estacion");
                                    String region = (String) cultivoDoc.get("region");
                                    String crecimiento = (String) cultivoDoc.get("crecimiento");

                                    // se crea un nuevo objeto "cultivo" en base a la info extraida
                                    CultivosGenerador cultivo = new CultivosGenerador(
                                            ID,
                                            nombre,
                                            tipo,
                                            crecimiento,
                                            info,
                                            temperatura,
                                            estacion,
                                            region,
                                            icono
                                    );
                                    Log.i("sarchbarget", "se agrega cultivo que coincide con la busqueda");
                                    addCultivo(cultivo);
                                }
                                msjExito.show();
                                // se notifica de los cambios al adapter
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            msjError.show();
                            Log.w("errorBD", "error al conectar con la BD", e);
                        }
                    });
        }


    }
}
package com.maid.gardeningfriend.recomendaciones;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maid.gardeningfriend.panelAdmin.CultivosDetallesParceable;
import com.maid.gardeningfriend.login.Login;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

import java.util.ArrayList;
import java.util.HashMap;
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
    ArrayList<CultivosGenerador> cultivosRecomendaciones = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendaciones_cultivos);

        //se recibe el objeto parceable con los datos
        CultivosRecoParceable opcSeleccionadas = getIntent().getParcelableExtra("datosReco");

        // se reciben los datos selec en la pantalla anterior
        tempSelec = opcSeleccionadas.getTemperaturaSelec();
        estSelec = opcSeleccionadas.getEstacionSelec();
        regSelec = opcSeleccionadas.getRegSelec();

        Log.i("prueba", tempSelec);
        Log.i("prueba", estSelec);
        Log.i("prueba", regSelec);

        //se inicializa funcion para agregar las tarjetas
        addModelsCultivos();

    }

    /**
     * agrega los cultivos que coinciden con los param selecionados por el user
     */
    private void addModelsCultivos(){
        //mensaje error de conexion
        Toast msjErrorBD = Toast.makeText(RecomendacionesCultivos.this,
                "ha ocurrido un error al conectar con la BD",
                Toast.LENGTH_SHORT);

        // 1 - se crea una instancia de la BD para acceder a la coleccion
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2 - se realiza una get request para consumir los datos de los cultivos
        db.collection("cultivos")
                // queries para filtrar el cultivo de acuerdo
                // a la seleccion del user
                .whereEqualTo("temperatura", tempSelec)
                .whereEqualTo("estacion", estSelec)
                .whereEqualTo("region", regSelec)
                .get()
                //listener que verifica si la peticion fue exitosa
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("tag","peticion exitosa");
                            // se recorre la coleccion y se extraen los datos necesarios
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // se extraen las propiedades del elem iterado
                                String nombre = (String) document.get("nombre");
                                String temperatura = (String) document.get("temperatura");
                                String estacion = (String) document.get("estacion");
                                String region = (String) document.get("region");
                                String informacion = (String) document.get("informacion");
                                String crecimiento = (String) document.get("crecimiento");
                                String tipo = (String) document.get("tipo");
                                String imagen = (String) document.get("icono");

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

                                Log.i("prueba", nuevoCultivo.getID());
                                Log.i("prueba", nuevoCultivo.getNombre());
                                agregarCultivo(nuevoCultivo);

                            }
                            // una vez que se iteran todos los documentos se activa el recyclerview adapter
                            activarAdapter();

                            Log.i("tag", "tamaño array: " + cultivosRecomendaciones.size());
                        } else {
                            // fracaso la peticion & se muestra mensaje
                            Toast.makeText(RecomendacionesCultivos.this,"hubo un error al conectarse con la BD", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        msjErrorBD.show();
                        Log.w("errorBD", "error conexion BD: ", e);
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
        cultivosRecomendaciones.add(cultivoSelec);
        Log.i("tag", "cultivo agreagado: " + cultivoSelec.nombre);
    }

    /**
     * activa el adapter de recycler view
     * para pasar todos los cultivos
     * guardados en el array "cultivosFiltrados"
     */
    public void activarAdapter(){
        // mensaje de alerta si no hay coincidencias (si el array esta vacio)
        if(cultivosRecomendaciones.isEmpty()){
            Toast.makeText(RecomendacionesCultivos.this,
                    "no se ha encontrado ningun resultado que coincida con los parametros seleccionados",
                    Toast.LENGTH_SHORT).show();
            // mensaje de sugerencia:
            Toast.makeText(RecomendacionesCultivos.this,
                    "intenta seleccioando parametros diferentes",
                    Toast.LENGTH_SHORT).show();

        } else {
            //se identifica el recyclerview de la activity
            RecyclerView recycler = findViewById(R.id.rvcultivos);
            // se activa el "adapter" para que pase las tarjetas al recycler
            RecomendacionesRecyclerView adapter = new RecomendacionesRecyclerView(this, cultivosRecomendaciones,this);
            recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recycler.setLayoutManager(new LinearLayoutManager(this));
        }
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
        Intent intent = new Intent(RecomendacionesCultivos.this, RecomendacionesDetalles.class);

        // 2 - se crea objeto parceable para pasar las propiedades del cult selec a la siguiente
        //pantalla
        CultivosDetallesParceable detallesCultivo = new CultivosDetallesParceable(
                cultivosRecomendaciones.get(position).getNombre(),
                cultivosRecomendaciones.get(position).getTemperatura(),
                cultivosRecomendaciones.get(position).getEstacionSiembra(),
                cultivosRecomendaciones.get(position).getRegion(),
                cultivosRecomendaciones.get(position).getCaracteristicas(),
                cultivosRecomendaciones.get(position).getImagen(),
                cultivosRecomendaciones.get(position).getTipo(),
                cultivosRecomendaciones.get(position).getDuracionCrecimiento()
        );

        //prueba
        Log.i("tag", "nombre cultivo: " + detallesCultivo.getNombreCultivo());

        // 3 - se pasa el objeto parceable al intent
        intent.putExtra("CULTIVO_DETALLES", detallesCultivo);

        // 4  - se inicialza la nueva actividad (intent)
        startActivity(intent);
    }

    /**
     * identifica el usuario y añade el cultivo
     * a su lista de favoritos
     * @param position
     * index numerico de la tarjeta
     */
    @Override
    public void onFavClick(int position) {
        String cultivoSelec = cultivosRecomendaciones.get(position).getNombre();
        getListaFavs(cultivoSelec);
    }


    /**
     * extrae la lista de cultivos del usuario
     * @param nombreCultivo
     * cultivo que se selecciono como fav
     */
    private void getListaFavs(String nombreCultivo){
        // mensajes de alerta
        Toast msjExito = Toast.makeText(RecomendacionesCultivos.this,
                "conexion exitosa con la BD",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(RecomendacionesCultivos.this,
                "no se ha podido conectar con la BD",
                Toast.LENGTH_SHORT);

        Toast msjRepetido = Toast.makeText(RecomendacionesCultivos.this,
                "este cultivo ya se encuentra en favoritos!",
                Toast.LENGTH_SHORT);

        // se crea instancia de BD y autenticacion
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // validacion logueo:
        if (auth.getCurrentUser() != null){
            // se extrae ID del usuario
            String userID = auth.getCurrentUser().getUid();
            // se realiza peticion GET
            db.collection("usuarios")
                    .document(userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                msjExito.show();
                                //flag
                                boolean repetido = false;
                                DocumentSnapshot documentSnapshot = task.getResult();
                                // se guardan los datos del usuario en un hashmap
                                Map<String, Object> userDoc = documentSnapshot.getData();
                                // se guarda la lista de favoritos
                                ArrayList<String> favoritos = (ArrayList<String>) userDoc.get("favoritos");
                                // se comprueba que el cultivo no exista en la lista
                                for (String fav: favoritos) {
                                    if (fav.equals(nombreCultivo)){
                                        repetido = true;
                                        msjRepetido.show();
                                    }
                                }
                                if (!repetido){
                                    favoritos.add(nombreCultivo);
                                    agregarCultivoFav(favoritos, userID);
                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            msjError.show();
                            Log.w("tag", "error al conectar con la BD", e);
                        }
                    });

        } else {
            // el usuario es redirigido al login
            Intent intent = new Intent(RecomendacionesCultivos.this, Login.class);
            startActivity(intent);
        }


    }

    /**
     * realiza una UPDATE REQUEST para actualizar la lista de favs
     * @param listaFav
     * lista de fav actualizada
     * @param userID
     * ID del usuario
     */
    private void agregarCultivoFav(ArrayList<String> listaFav, String userID){
        // mensajes de alerta
        Toast msjExito = Toast.makeText(RecomendacionesCultivos.this,
                "favoritos actualizados",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(RecomendacionesCultivos.this,
                "se ha podido actualizar favoritos",
                Toast.LENGTH_SHORT);

        // se crea instancia de la BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> favoritosUser = new HashMap<>();
        favoritosUser.put("favoritos", listaFav);

        // se actualiza el documento del usuario con el nuevo favorito:
        db.collection("usuarios")
                .document(userID)
                .update(favoritosUser)
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
                        Log.w("tag", "error al conectar con BD: ", e);
                    }
                });

    }
}
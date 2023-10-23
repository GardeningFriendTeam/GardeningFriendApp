package com.maid.gardeningfriend.favoritos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maid.gardeningfriend.CultivosDetallesParceable;
import com.maid.gardeningfriend.CultivosGenerador;
import com.maid.gardeningfriend.Login;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.RecomendacionesDetalles;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * en esta pantalla se muestran las tarjetas
 * guardadas en favs, consumiendo los datos
 * de FireStore (atributo favoritos del usuario)
 */
public class FavoritosSeccion extends MainActivity implements FavoritosInterface{
    // Atributos
    // contiene el array "favoritos" del usuario:
    ArrayList<String> cultivosFavoritos = new ArrayList<>();
    // contiene cada cultivo favorito con todos sus detalles:
    ArrayList<CultivosGenerador> cultivosFavsInfo = new ArrayList<>();

    FavoritosRecyclerView favoritosAdapter;

    RecyclerView favoritosRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);;

        authUser();

        // se identifica barra de busquedas y se agregan los eventos:
        SearchView searchView = findViewById(R.id.buscador_favs);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String texto) {
                Log.i("searchbar", "buscar texto");
                barraBusqueda(texto);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                Log.i("searchbar", "el texto se ha modificado");
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cultivosFavsInfo.clear();
                authUser();
                return false;
            }
        });
    }

    /**
     * identifica al usuario logueado
     * y se extrae su ID
     */
    private void authUser(){
        // se crea instancia de Firebase user:
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String usuarioID;

        if(auth.getCurrentUser() != null){
            // si el usuario esta logueado
            Log.i("auth", "usuario autenticado");
            getUsuarioFav(user);

        } else {
            // en caso de no estarlo es llevalo al login
            Intent intent = new Intent(FavoritosSeccion.this, Login.class);
            startActivity(intent);
        }

    }

    /**
     * extrae el doc del usuario de la BD
     * en base a su ID
     * @param user
     */
    private void getUsuarioFav(FirebaseUser user){
        // 1 - se crea instancia de BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = user.getUid();

        // 2 - se realiza peticion get para extraer la info de favoritos
        db.collection("usuarios")
                .document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            // se extrae el usuario en un hashmap
                            DocumentSnapshot userDoc = task.getResult();
                            Map<String, Object> userMap = userDoc.getData();
                            ArrayList<String> userFavs = (ArrayList<String>) userMap.get("favoritos");
                            Log.i("getUsuario", userFavs.toString());
                            if (!userFavs.isEmpty()){
                                // se agrega validacion para evitar
                                // error "nullPointerException"
                                getCultivosFav(userFavs);
                            }

                        }
                    }
                }). addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("getUser", "error conexion BD: ", e);
                    }
                });
    }

    /**
     * extrae los cultivos de la BD
     * en funcion de los favs seleccionados por el user
     * @param favoritos
     * contiene todos los favs
     */
    private void getCultivosFav(ArrayList<String> favoritos){
        // log para testear
        Log.i("getCultivos0", favoritos.toString());

        // 1 - se crean las instancias necesarias de la BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cultivosCollection = db.collection("cultivos");

        // 2 - SE REALIZA LA GET REQUEST:
        cultivosCollection.whereIn("nombre", favoritos)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for ( QueryDocumentSnapshot document : task.getResult() ) {
                                // se extrae la informacion de cada documento
                                Map<String, Object> cultivoDoc = document.getData();

                                String cultivoID = (String) cultivoDoc.get("id");
                                String cultivoNombre = (String) cultivoDoc.get("nombre");
                                String cultivoInfo = (String) cultivoDoc.get("informacion");
                                String cultivoCrecimiento = (String) cultivoDoc.get("duracion");
                                String cultivoIcono = (String) cultivoDoc.get("icono");
                                String cultivoTipo = (String) cultivoDoc.get("tipo");
                                String cultivoTemp = (String) cultivoDoc.get("temperatura");
                                String cultivoEst = (String) cultivoDoc.get("estacion");
                                String cultivoReg = (String) cultivoDoc.get("region");

                                CultivosGenerador cultivo = new CultivosGenerador(
                                        cultivoID,
                                        cultivoNombre,
                                        cultivoTipo,
                                        cultivoCrecimiento,
                                        cultivoInfo,
                                        cultivoTemp,
                                        cultivoEst,
                                        cultivoReg,
                                        cultivoIcono
                                );
                                Log.i("getCultivos1", cultivo.getNombre());
                                addCultivo(cultivo);
                            }
                            setAdapterFavs();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("getCultivosError", "error BD", e);
                    }
                });

    }

    /**
     * agrega el cultivo al array
     * 'cultivosFavsInfo'
     * @param cultivo
     */
    private void addCultivo(CultivosGenerador cultivo){
        cultivosFavsInfo.add(cultivo);
    }

    /**
     * activa el adapter pasando todos
     * los cultivos a este
     */
    private void setAdapterFavs(){
        Log.i("setAdapter", "adaptador activado");
        favoritosRecyclerView = findViewById(R.id.recyclerview_favs);
        favoritosAdapter = new FavoritosRecyclerView(this,cultivosFavsInfo,this);
        favoritosRecyclerView.setAdapter(favoritosAdapter);
        favoritosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * funcion que lleva al usuario
     * a la siguiente pantalla donde se muestra toda
     * la info
     * @param position
     * index tarjeta
     */
    @Override
    public void OninfoFav(int position) {
        // se crea el intent
        Intent intent = new Intent(FavoritosSeccion.this, RecomendacionesDetalles.class);

        //se crea objeto parceable para enviar la informacion via intent
        CultivosDetallesParceable parceable = new CultivosDetallesParceable(
                cultivosFavsInfo.get(position).getNombre(),
                cultivosFavsInfo.get(position).getTemperatura(),
                cultivosFavsInfo.get(position).getEstacionSiembra(),
                cultivosFavsInfo.get(position).getRegion(),
                cultivosFavsInfo.get(position).getCaracteristicas(),
                cultivosFavsInfo.get(position).getImagen(),
                cultivosFavsInfo.get(position).getTipo(),
                cultivosFavsInfo.get(position).getDuracionCrecimiento()
        );

        //se vincula el parceable con el intent
        intent.putExtra("CULTIVO_DETALLES", parceable);

        // se inicia la actividad
        startActivity(intent);

    }


    /**
     * funcion que elimina el cultivo de favoritos
     * @param position
     * index tarjeta
     */
    @Override
    public void OnEliminarFav(int position) {
        // 1 - se identifica el usuario actual
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // propiedades varias
        FirebaseUser user;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> userFavs;
        String cultivoEliminar = cultivosFavsInfo.get(position).getID();

        //2 - se elimina el elemento del array local para evitar hacer otra get request
        // (luego es updated al doc del user)
        // se usa un iterator para evitar errores de concurrencia
        Iterator<String> iter = cultivosFavoritos.iterator();
        while (iter.hasNext()){
            String element = iter.next();
            if(element.equals(cultivoEliminar)){
                iter.remove();
            }
        }

        //mensajes alerta
        Toast msjLogueo = Toast.makeText(FavoritosSeccion.this, "" +
                "debes estar logueado para eliminar el cultivo!",
                Toast.LENGTH_SHORT);

        Toast msjExito = Toast.makeText(FavoritosSeccion.this,
                "el cultivo se ha eliminado de favoritos, refresca" +
                        "la seccion para visualizar los cambios",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(FavoritosSeccion.this,
                "no se ha podido conectar con la BD",
                Toast.LENGTH_SHORT);

        // autenticacion (para obtener las propiedades del usuario actual)
        if (auth.getCurrentUser() != null){
            //user actual logueado:
            user = auth.getCurrentUser();

            // 3 - UPDATE REQUEST (se pasa el nuevo array con el cultivo eliminado)
            db.collection("usuarios")
                    .document(user.getUid())
                    .update("favoritos", cultivosFavoritos)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                msjExito.show();
                                //favoritosAdapter.notifyDataSetChanged();
                                Log.i("array: ", cultivosFavoritos.toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            msjError.show();
                            Log.w("error", "error al conectar con la BD", e);
                        }
                    });


        } else {
            msjLogueo.show();
        }
    }

    /**
     * toma el input del usuario, lo valida
     * y actualiza el array que se pasa al adapter
     * @param input
     * palabra ingresada
     */
    private void barraBusqueda(String input){
        //mensajes de alerta
        Toast msjExito = Toast.makeText(FavoritosSeccion.this,
                "resultados de busqueda: ",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(FavoritosSeccion.this,
                "no se encontraron resultados",
                Toast.LENGTH_SHORT);

        Toast errorBD = Toast.makeText(FavoritosSeccion.this,
                "error al conectar con la BD",
                Toast.LENGTH_SHORT);

        // 1 - se crea instancia de BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2 - se valida que la palabra este en favoritos
        Log.i("arrayfav", cultivosFavsInfo.toString());

        boolean flagInput = false;
        for (CultivosGenerador cultivo : cultivosFavsInfo){
            if(cultivo.getNombre().equals(input.toLowerCase())){
                flagInput = true;
            }
        }

        // 3 - se realiza la get request si la flag esta activada
        if(flagInput){
            // se limpia el array del adapter primero
            cultivosFavsInfo.clear();

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
                                favoritosAdapter.notifyDataSetChanged();
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

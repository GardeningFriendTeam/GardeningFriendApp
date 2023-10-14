package com.maid.gardeningfriend.favoritos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.context.AttributeContext;
import com.maid.gardeningfriend.CultivosGenerador;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * en esta pantalla se muestran las tarjetas
 * guardadas en favs, consumiendo los datos
 * de FireStore (atributo favoritos del usuario)
 */
public class Favoritos extends MainActivity {
    // Atributos
    ArrayList<String> cultivosFavoritos = new ArrayList<>();
    ArrayList<CultivosGenerador> cultivosFavsInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        // se obtiene el usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // se lo pasa como argumento a la funcion que hace el get request:
        getFavsUser(user);

    }

    /**
     * realizar una peticion GET a la BD para extraer todos los
     * elementos guardados en el atributo "favoritos" del usuario actual
     */
    private void getFavsUser(FirebaseUser firebaseUser){
        // 1 - se crea instancia de FireStore para trabajar con la BD
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // se identifica el usuario en la BD
        DocumentReference docRef = db.collection("usuarios").document(firebaseUser.getUid());

        //mensajes de alerta
        Toast msjExito = Toast.makeText(Favoritos.this,
                "conexión establecida con la BD ",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(Favoritos.this,
                "no se ha podido conectar con la BD",
                Toast.LENGTH_SHORT);

        // 2 - se realizar una get request
        //TODO: BUSCAR UNA FORMA DE REALIZAR PETICION SOBRE ATRIBUTO ARRAY DIRECTAMENTE
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            // se guarda la informacion extraida en un hashmap
                            Map<String, Object> user = document.getData();
                            ArrayList<String> userFavs = (ArrayList<String>) document.get("favoritos");

                            getCultivos(firebaseUser.getUid(), userFavs);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        msjError.show();
                        Log.w("tag", "error al conectar con la BD", e);
                    }
                });

    }

    /**
     * extrae los cultivos favoritos de
     * la coleccion en base a su ID
     * @param userID
     * ID del user
     * @param listaFavs
     * Arraylist con todos los favs
     */
    private void getCultivos(String userID, ArrayList listaFavs){
        // se completa el array
        cultivosFavoritos = listaFavs;

        //mensajes de alerta
        Toast msjExito = Toast.makeText(Favoritos.this,
                "se obtuvieron todos los cultivos",
                Toast.LENGTH_SHORT);

        Toast msjError = Toast.makeText(Favoritos.this,
                "no se pudieron obtener los cultivos",
                Toast.LENGTH_SHORT);

        // se realiza una peticion get para obtener la info de todos
        // los cultivos guardados en favoritos:
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference coleccionCultivos = db.collection("cultivos");

        for (String IDcultivo : cultivosFavoritos) {
                coleccionCultivos
                        .whereEqualTo("id", IDcultivo)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //se extrae el documento recibido en un haspmap
                                        Map<String, Object> cultivoDocument = document.getData();

                                        String idCultivo = (String) cultivoDocument.get("id");
                                        String nombreCultivo = (String) cultivoDocument.get("nombre");
                                        String infoCultivo = (String) cultivoDocument.get("info");
                                        String crecCultivo = (String) cultivoDocument.get("crecimiento");
                                        String iconoCultivo = (String) cultivoDocument.get("icono");
                                        String tempCultivo = (String) cultivoDocument.get("temperatura");
                                        String estCultivo = (String) cultivoDocument.get("estacion");
                                        String regCultivo = (String) cultivoDocument.get("region");
                                        String tipoCultivo = (String) cultivoDocument.get("tipo");

                                        //se crea un nuevo objeto de tipo "cultivo" y se lo añade al array "cultivoFavsInfo"
                                        CultivosGenerador cultivo = new CultivosGenerador(
                                                idCultivo,
                                                nombreCultivo,
                                                tipoCultivo,
                                                crecCultivo,
                                                infoCultivo,
                                                tempCultivo,
                                                estCultivo,
                                                regCultivo,
                                                iconoCultivo
                                        );

                                        addCultivos(cultivo);
                                    }
                                    setAdapterFav();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                msjError.show();
                                Log.w("tag", "no se pudieron obtner los cultivos: ", e);
                            }
                        });
        }



    }

    /**
     * añade el cultivo iterado
     * en el array 'cultivosFavsInfo'
     * para luego usarlo como base
     * en el recyclerview adapter
     * @param cultivo
     * cultivo iterado
     */
    private void addCultivos(CultivosGenerador cultivo){
        cultivosFavsInfo.add(cultivo);
    }



    /**
     * agrega el adapter al recyclerView
     * una vez que se completo la iteracion del array
     */
    public void setAdapterFav(){
        // se identifica el recycler view
        RecyclerView recyclerViewFav = findViewById(R.id.recyclerview_favs);

        // se activa su adaptador
        FavoritosRecyclerView adapter = new FavoritosRecyclerView(this, cultivosFavsInfo);
        recyclerViewFav.setAdapter(adapter);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));
    }

}
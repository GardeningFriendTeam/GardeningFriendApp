package com.maid.gardeningfriend;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Contiene toda la logica que maneja la implementacion
 * de recyclerview para mostrar todas las tarjetas de los usuarios
 */
public class PanelAdminUsuarios extends MainActivity {

    //ATRIBUTOS
    // representa los usuarios extraidos de la BD
    private ArrayList<UsuariosGenerador> usuariosBD = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_usuarios);

        // se llama a la funcion para consumir los usuarios de la BD
        getUsuarios();
    }

    /**
     * realiza una GET request a la BD
     * para extraer todos los usuarios representadolos
     * como objetos
     */
    public void getUsuarios(){
        // 1 - se crea instancia de FireStore:
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2 - se realiza peticion:
        db.collection("usuarios")
                .get()
                // listener que determina si la operacion se completo
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            // se usa un bucle para recorrer todos los docs
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                // se crea un hash para almacenar la info de cada user
                                Map<String, Object> data = document.getData();

                                String ID = (String) data.get("id");
                                String name = (String) data.get("name");
                                String email = (String) data.get("email");
                                ArrayList<String> favoritos = (ArrayList<String>) data.get("favoritos");
                                boolean isAdmin = (boolean) data.get("isAdmin");

                                // se crea objeto para agregar a arraylist base
                                UsuariosGenerador usuario = new UsuariosGenerador(
                                        ID,
                                        name,
                                        email,
                                        favoritos,
                                        isAdmin
                                );

                                //funcion que agrega el objeto al arrayList:
                                agregarUsuario(usuario);
                            }
                            // una vez que se iteraron todos los documentos
                            // se activa el adapter
                            setAdapter();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PanelAdminUsuarios.this,
                                "ocurrio un error al conectar con la BD",
                                Toast.LENGTH_SHORT).show();

                        Log.d("tag", "error al conectar con la BD: ", e);

                    }
                });


    }

    /**
     * agrega el doc iterado al array
     * donde se alamacenan todos los usuarios
     * @param usuario
     * objeto del usuario
     */
    private void agregarUsuario(UsuariosGenerador usuario){
        usuariosBD.add(usuario);
    }

    public void setAdapter(){
        //se identifica el recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerview_user_padmin);
        // se vincula su adaptador
        PanelAdminUserRecycler adapter = new PanelAdminUserRecycler(this,usuariosBD);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
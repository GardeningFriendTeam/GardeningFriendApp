package com.maid.gardeningfriend.panelAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.recetas.RecetasAdapter;
import com.maid.gardeningfriend.recomendaciones.CultivosGenerador;

import java.util.ArrayList;
import java.util.Map;

public class PanelAdminRecetas extends AppCompatActivity implements PanelAdminInterfaceRecetas {
    // atributos
    RecyclerView recyclerViewRecetas;
    PanelAdminRecetasAdapter adapterRecetas;
    ArrayList<RecetasGenerador> recetas = new ArrayList<RecetasGenerador>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("recetas");

    FloatingActionButton btnAddNewReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_recetas);
        // identifying btn
        btnAddNewReceta = findViewById(R.id.btn_add_new_receta);
        btnAddNewReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelAdminRecetas.this,PanelAdminAgregarReceta.class);
                startActivity(intent);
            }
        });
        // fetching recetas
        getRecetas();
    }


    private void getRecetas(){
        // referencing collection
        collectionRef
                // executing request
                .get()
                // handling results
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // iterating over results
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Deserialize each document here
                        RecetasGenerador receta = document.toObject(RecetasGenerador.class);
                        // Document is added to arraylist
                        agregarReceta(receta);
                    }
                    // once all documents are deserialez and store locally
                    // the recyclerview is activated
                    activarAdapter();
                } else {
                    // showing error
                    Log.e("FETCH_RECETAS_ADMIN_PANEL", "FETCH FAILED!", task.getException());
                    Toast.makeText(
                            PanelAdminRecetas.this,
                            "fallo la conexión con la BD!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void agregarReceta(RecetasGenerador receta){
        recetas.add(receta);
    }

    public void activarAdapter(){
        //se identifica el recycler view
        recyclerViewRecetas = findViewById(R.id.recycler_panel_recetas);
        // se crea el adapter para recyclerview
        adapterRecetas = new PanelAdminRecetasAdapter(this, recetas,this);
        recyclerViewRecetas.setAdapter(adapterRecetas);
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void eliminarBtn(int position) {
        // identifing elem selected
        RecetasGenerador recetaSelec = recetas.get(position);
        // document reference
        DocumentReference documentReference = db.collection("recetas").document(recetaSelec.id);
        // executing request
        documentReference
                .delete()
                // handling results
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // success message
                            Toast.makeText(
                                    PanelAdminRecetas.this,
                                    "el elemento ha sido eliminado de la BD",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // error message
                            Toast.makeText(
                                    PanelAdminRecetas.this,
                                    "ha ocurrido un error al procesar la solicitud",
                                    Toast.LENGTH_SHORT).show();
                            // console log
                            Log.e("REMOVE_RECETA", "OPERATION_FAILED", task.getException());
                        }
                    }
                });
        // removing element locally
        adapterRecetas.removeItem(position);
    }

    @Override
    public void editarBtn(int position) {
        // creating intent
        Intent intent = new Intent(PanelAdminRecetas.this,PanelAdminEditarRecetas.class);
        // passing data via serializable
        intent.putExtra("RECETA_SELEC", recetas.get(position));
        // starting activity
        startActivity(intent);
    }
}
package com.maid.gardeningfriend.recetas;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

public class RecetasActivity extends MainActivity {

    RecyclerView recyclerViewRecetas;

    private static final String TAG = "RecetasActivity";
    RecetasAdapter recetasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);

        // Obtener referencias a elementos de la interfaz de usuario
        recyclerViewRecetas = findViewById(R.id.recycler_recetas);

        // Configura y muestra las recetas en el RecyclerView
        setupRecyclerView();

    };

    // Configura el RecyclerView para mostrar las recetas
    void setupRecyclerView() {
        // Obtener una consulta de Firestore para las recetas
        Query query = FirebaseFirestore.getInstance().collection("recetas");
        // Configurar las opciones del adaptador para Firebase Firestore
        FirestoreRecyclerOptions<Receta> options = new FirestoreRecyclerOptions.Builder<Receta>()
                .setQuery(query, Receta.class).build();
        // configurar el administrador de diseño y el adaptador para el RecyclerView
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
        recetasAdapter = new RecetasAdapter(options, this);
        recyclerViewRecetas.setAdapter(recetasAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recetasAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recetasAdapter != null) {
            recetasAdapter.stopListening();
        }
    }

    protected void onResume() {
        super.onResume();
        if (recetasAdapter != null) {
            recetasAdapter.notifyDataSetChanged();
        }
    }

}
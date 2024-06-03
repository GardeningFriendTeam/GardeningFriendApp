package com.maid.gardeningfriend.recetas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

public class RecetasActivity extends MainActivity {

    RecyclerView recyclerViewRecetas;
    Spinner spinnerCategoria;
    BottomNavigationView bottomNavigation;

    RecetasAdapter recetasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_recetas, findViewById(R.id.content_frame));

        // Mostrar u Ocultar el menu inferior
        bottomNavigation = findViewById(R.id.barraMenu);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        bottomNavigation = findViewById(R.id.barraMenu);
        if (auth.getCurrentUser() != null){
            bottomNavigation.setVisibility(View.VISIBLE);
        }else {
            bottomNavigation.setVisibility(View.GONE);
        }


        // Obtener referencias a elementos de la interfaz de usuario
        recyclerViewRecetas = findViewById(R.id.recycler_recetas);
        spinnerCategoria = findViewById(R.id.spinner_categoria);

        // Configurar el Spinner
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoria = parent.getItemAtPosition(position).toString();
                setupRecyclerView(categoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Configura y muestra las recetas en el RecyclerView
        setupRecyclerView("Todas");
    }

    // Configura el RecyclerView para mostrar las recetas
    void setupRecyclerView(String categoria) {
        // Obtener una consulta de Firestore para las recetas
        Query query;
        if (categoria.equals("Todas")) {
            query = FirebaseFirestore.getInstance().collection("recetas");
        } else {
            query = FirebaseFirestore.getInstance().collection("recetas")
                    .whereEqualTo("categoria", categoria);
        }

        // Configurar las opciones del adaptador para Firebase Firestore
        FirestoreRecyclerOptions<Receta> options = new FirestoreRecyclerOptions.Builder<Receta>()
                .setQuery(query, Receta.class)
                .build();

        // configurar el administrador de diseño y el adaptador para el RecyclerView
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
        if (recetasAdapter != null) {
            recetasAdapter.stopListening();
        }
        recetasAdapter = new RecetasAdapter(options, this);
        recyclerViewRecetas.setAdapter(recetasAdapter);
        recetasAdapter.startListening();
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
package com.maid.gardeningfriend.panelAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.MainActivity;
import com.maid.gardeningfriend.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelAdminAgregarReceta extends MainActivity{
    // atributos
    EditText nombre;
    EditText categoria;
    EditText instrucciones;
    EditText cookTime;
    EditText urlImagen;
    EditText ingrediente1;
    EditText ingrediente2;
    EditText ingrediente3;

    Button btnAddReceta;

    FirebaseFirestore bd = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin_agregar_receta);

        // identificando elementos de layout
        nombre = findViewById(R.id.editText_nombre_receta);
        categoria = findViewById(R.id.editText_categoria_receta);
        instrucciones = findViewById(R.id.editText_instrucciones_receta);
        cookTime = findViewById(R.id.editText_cookTime_receta);
        urlImagen = findViewById(R.id.editText_url_receta);
        ingrediente1 = findViewById(R.id.editText_ingrediente1_receta);
        ingrediente2 = findViewById(R.id.editText_ingrediente2_receta);
        ingrediente3 = findViewById(R.id.editText_ingrediente3_receta);
        btnAddReceta = findViewById(R.id.btn_add_receta);
        btnAddReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceta();
            }
        });

    }

    private void addReceta(){
        // creating arraylist with ingredients
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(ingrediente1.getText().toString());
        ingredients.add(ingrediente2.getText().toString());
        ingredients.add(ingrediente3.getText().toString());

        // reference object
        RecetasGenerador recetasGenerador = new RecetasGenerador(
                nombre.getText().toString(),
                categoria.getText().toString(),
                cookTime.getText().toString(),
                urlImagen.getText().toString(),
                ingredients,
                instrucciones.getText().toString(),
                ""
        );

        // request
        bd.collection("recetas")
                // executing request
                .add(recetasGenerador)
                // handling results
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            // success message
                            Toast.makeText(
                                    PanelAdminAgregarReceta.this,
                                    "la receta se ha agregado a la BD",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // error message
                            Toast.makeText(
                                    PanelAdminAgregarReceta.this,
                                    "ha ocurrido un error, intenta de nuevo",
                                    Toast.LENGTH_SHORT).show();
                            // console log
                            Log.e("AGREGAR_RECETA", "OPERATION FAILED", task.getException());
                        }
                    }
                });
    }
}
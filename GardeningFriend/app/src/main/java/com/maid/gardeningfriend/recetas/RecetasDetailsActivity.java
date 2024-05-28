package com.maid.gardeningfriend.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

public class RecetasDetailsActivity extends AppCompatActivity {

    TextView recetaNombreTextView, recetaIngredientesTextView,
            recetaCookTimeTextView, recetaInstruccionesTextView,
            recetaCategoriaTextView;
    ImageView recetaImageViewDetails;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas_details);

        // obtener referencias a los elementos de la interfaz
        recetaNombreTextView = findViewById(R.id.textViewRecipeName);
        recetaIngredientesTextView = findViewById(R.id.textViewIngredients);
        recetaCookTimeTextView = findViewById(R.id.textViewCookTime);
        recetaInstruccionesTextView = findViewById(R.id.textViewInstructions);
        recetaCategoriaTextView = findViewById(R.id.textViewCategory);
        recetaImageViewDetails = findViewById(R.id.imageViewRecipeDetails);

        Intent intent = getIntent();
        if (intent != null) {
            String nombre = intent.getStringExtra("nombre");
            String cookTime = intent.getStringExtra("cookTime");
            String ingredientes = intent.getStringExtra("ingredientes");
            String instrucciones = intent.getStringExtra("instrucciones");
            String categoria = intent.getStringExtra("categoria");
            String imageUrl = intent.getStringExtra("imageUrl");

            recetaNombreTextView.setText(nombre);
            recetaCookTimeTextView.setText("tiempo de elaboracion: " + cookTime);
            recetaIngredientesTextView.setText("Ingredientes: \n" + ingredientes);
            recetaInstruccionesTextView.setText("Instrucciones: \n" + instrucciones);
            recetaCategoriaTextView.setText("Categoria: " + categoria);

            Picasso.get().load(imageUrl).into(recetaImageViewDetails);
        }

    }


}
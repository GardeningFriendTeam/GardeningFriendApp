package com.maid.gardeningfriend.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
            recetaCookTimeTextView.setText(Html.fromHtml("<h3>Tiempo de Preparacion:</h3>"+cookTime, Html.FROM_HTML_MODE_LEGACY));
            recetaIngredientesTextView.setText(Html.fromHtml("<h3>Ingredientes:</h3>"+ingredientes, Html.FROM_HTML_MODE_LEGACY));
            recetaCategoriaTextView.setText(Html.fromHtml("<h3>Categoria:</h3>"+categoria, Html.FROM_HTML_MODE_LEGACY));
            recetaInstruccionesTextView.setText(Html.fromHtml("<h3>Instrucciones:</h3>"+instrucciones, Html.FROM_HTML_MODE_LEGACY));

            Picasso.get().load(imageUrl).into(recetaImageViewDetails);
            recetaImageViewDetails.setContentDescription("Imagen de "+nombre);
        }

    }


}
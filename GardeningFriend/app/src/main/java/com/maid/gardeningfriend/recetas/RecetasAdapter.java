package com.maid.gardeningfriend.recetas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

public class RecetasAdapter extends FirestoreRecyclerAdapter<Receta, RecetasAdapter.RecetaViewHolder> {
    Context context;
    // Constructor que toma las opciones del adaptador y el contexto
    public RecetasAdapter(@NonNull FirestoreRecyclerOptions<Receta> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull RecetasAdapter.RecetaViewHolder holder, int position, @NonNull Receta receta) {
        // Establecer los valores en las vistas
        holder.nombreRecetaTextView.setText(receta.getNombre());
        holder.cookTimeTextView.setText(receta.getCookTime());
        Picasso.get().load(receta.getImageUrl()).into(holder.recetaImageView);

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, RecetasDetailsActivity.class);
            intent.putExtra("nombre", receta.getNombre());
            intent.putExtra("cookTime", receta.getCookTime());
            intent.putExtra("ingredientes", String.join("<br /><br />- ", receta.getIngredientes()));
            intent.putExtra("instrucciones", receta.getInstrucciones());
            intent.putExtra("categoria", receta.getCategoria());
            intent.putExtra("imageUrl", receta.getImageUrl());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    // Método llamado para crear una vista del elemento receta en el RecyclerView
    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista de elemento de receta desde un diseño XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recetas_item, parent, false);
        return new RecetaViewHolder(view);
    }

    // Clase interna que representa la vista de un elemento de receta en el RecyclerView
    class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreRecetaTextView, cookTimeTextView;
        ImageView recetaImageView;
        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreRecetaTextView = itemView.findViewById(R.id.textViewRecipeName);
            cookTimeTextView = itemView.findViewById(R.id.textViewRecipeCookTime);
            recetaImageView = itemView.findViewById(R.id.imageViewRecipe);
        }
    }


}



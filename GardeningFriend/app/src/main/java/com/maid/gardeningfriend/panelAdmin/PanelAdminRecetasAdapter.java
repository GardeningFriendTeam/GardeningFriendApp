package com.maid.gardeningfriend.panelAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maid.gardeningfriend.R;
import com.maid.gardeningfriend.panelAdmin.PanelAdminInterfaceRecetas;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PanelAdminRecetasAdapter extends RecyclerView.Adapter<PanelAdminRecetasAdapter.MyViewHolder> {
    //atributos
    private final PanelAdminInterfaceRecetas panelAdminInterface;
    Context context;
    ArrayList<RecetasGenerador> recetas;

    //constructor
    public PanelAdminRecetasAdapter(Context context, ArrayList<RecetasGenerador> recetas, PanelAdminInterfaceRecetas panelAdminInterface) {
        this.context = context;
        this.recetas = recetas;
        this.panelAdminInterface = panelAdminInterface;
    }

    @NonNull
    @Override
    public PanelAdminRecetasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout con las tarjetas creadas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_recetas,parent,false);

        return new PanelAdminRecetasAdapter.MyViewHolder(view,panelAdminInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelAdminRecetasAdapter.MyViewHolder holder, int position) {
        // asigna un index a cada elem
        holder.titulo.setText(recetas.get(position).getNombre());
        // y extrae las propiedades de cada objeto del cultivo
        // a traves de la API de picasso se carga la imagen
        Picasso.get()
                .load(recetas.get(position).getImageUrl())
                .error(R.mipmap.logo)
                .into(holder.icono);

    }

    @Override
    public int getItemCount() {
        // identifica cuantos cultivos deben mostrarse
        return recetas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // basicamente crea las tarjetas de los cultivos
        // atributos
        ImageView icono;
        TextView titulo;
        Button btnEliminar, btnEditar;

        //constructor
        public MyViewHolder(@NonNull View itemView, PanelAdminInterfaceRecetas panelAdminInterface){
            super(itemView);
            icono = itemView.findViewById(R.id.ic_card_recetas);
            titulo = itemView.findViewById(R.id.nombre_receta);
            btnEditar = itemView.findViewById(R.id.btn_editar_receta);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_receta);

            //se agrega un click listener a ambos botones
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (panelAdminInterface != null){
                        // si no es nulo se extrae la posicion de la tarjeta
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            panelAdminInterface.editarBtn(pos);
                        }
                    }
                }
            });

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (panelAdminInterface != null){
                        // si no es nulo se extrae la posicion de la tarjeta
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            panelAdminInterface.eliminarBtn(pos);
                        }
                    }
                }
            });

        }
    }

    public void removeItem(int position) {
        if (recetas.size() > 1) {
            recetas.remove(position);
            notifyItemRemoved(position);
        }
    }

}

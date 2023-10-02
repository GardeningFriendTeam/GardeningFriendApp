package com.maid.gardeningfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PanelAdminRecyclerView extends RecyclerView.Adapter<PanelAdminRecyclerView.MyViewHolder> {
    //atributos
    Context context;
    ArrayList<CultivosGenerador> cultivos;

    //constructor
    public PanelAdminRecyclerView(Context context, ArrayList<CultivosGenerador> cultivos) {
        this.context = context;
        this.cultivos = cultivos;
    }

    @NonNull
    @Override
    public PanelAdminRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout con las tarjetas creadas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_cultivos_panel_admin,parent);

        return new PanelAdminRecyclerView.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelAdminRecyclerView.MyViewHolder holder, int position) {
        // asigna un index a cada elem
        // y extrae las propiedades de cada objeto del cultivo
        holder.titulo.setText(cultivos.get(position).getNombre());
        // a traves de la API de picasso se carga la imagen
        Picasso.get()
                .load(cultivos.get(position).getImagen())
                .error(R.mipmap.logo)
                .into(holder.icono);

    }

    @Override
    public int getItemCount() {
        // identifica cuantos cultivos deben mostrarse
        return cultivos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // basicamente crea las tarjetas de los cultivos
        // atributos
        ImageView icono;
        TextView titulo;

        //constructor
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            icono = itemView.findViewById(R.id.ic_card_cultivos);
            titulo = itemView.findViewById(R.id.nombre_cultivo);
        }
    }

}

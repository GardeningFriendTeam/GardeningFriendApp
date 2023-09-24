package com.maid.gardeningfriend;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * funcionalidades necesarias para insertar cultivos
 * en recyclerview
 */
public class RecomendacionesRecyclerView extends RecyclerView.Adapter<RecomendacionesRecyclerView.MyViewHolder> {
    // atributos
    Context context;
    ArrayList<CultivosGenerador> cultModel;

    // constrcutor
    public RecomendacionesRecyclerView(Context context, ArrayList<CultivosGenerador> cultModelo){
        this.context = context;
        this.cultModel = cultModelo;
    }

    @NonNull
    @Override
    public RecomendacionesRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "inflates" el layout con los estilos / modelos definidos
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_cultivos, parent, false);

        return new RecomendacionesRecyclerView.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomendacionesRecyclerView.MyViewHolder holder, int position) {
        // asigna un valor numerico representado la posicion de cada tarjeta
        holder.nombre.setText(cultModel.get(position).getNombre());
        holder.temperatura.setText(cultModel.get(position).getTemperatura());
        holder.estacion.setText(cultModel.get(position).getEstacionSiembra());
        holder.region.setText(cultModel.get(position).getRegion());
        holder.icono.setImageResource(cultModel.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        // solo cuenta cuandos modelos se han pasado al recyclerview
        return cultModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // es como una especie de 'oncreate'

        // atributos del modelo / tarjeta
        ImageView icono;
        TextView nombre, temperatura, estacion, region;

        //constructor
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            // se identifican los atributos de las tarjetas
            icono = itemView.findViewById(R.id.ic_cultivo);
            nombre = itemView.findViewById(R.id.titulo_cult_card);
            temperatura = itemView.findViewById(R.id.titulo_temp_card);
            estacion = itemView.findViewById(R.id.titulo_est_card);
            region = itemView.findViewById(R.id.titulo_reg_card);
        }
    }

}

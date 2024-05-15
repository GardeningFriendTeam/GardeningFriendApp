package com.maid.gardeningfriend.enciclopedia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maid.gardeningfriend.recomendaciones.CultivosGenerador;
import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EnciclopediaRecyclerViewAdapter extends RecyclerView.Adapter<EnciclopediaRecyclerViewAdapter.MyViewHolder>{
    //Atributos
    Context contextEnc;
    ArrayList<CultivosGenerador> cultivosEnc;

    EnciclopediaInterface interfaceEnc;

    //constructor
    public EnciclopediaRecyclerViewAdapter(Context contextEnc, ArrayList<CultivosGenerador> cultivosEnc, EnciclopediaInterface interfaceEnc) {
        this.contextEnc = contextEnc;
        this.cultivosEnc = cultivosEnc;
        this.interfaceEnc = interfaceEnc;
    }

    // inflater
    @NonNull
    @Override
    public EnciclopediaRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "inflates" el layout con los estilos / modelos definidos
        LayoutInflater inflater = LayoutInflater.from(contextEnc);
        View view = inflater.inflate(R.layout.cards_enciclopedia, parent, false);

        return new EnciclopediaRecyclerViewAdapter.MyViewHolder(view,interfaceEnc);
    }

    //binding
    @Override
    public void onBindViewHolder(@NonNull EnciclopediaRecyclerViewAdapter.MyViewHolder holder, int position) {
        // asigna un valor numerico representado la posicion de cada tarjeta
        holder.nombre.setText(cultivosEnc.get(position).getNombre());
        holder.temperatura.setText(cultivosEnc.get(position).getTemperatura());
        holder.estacion.setText(cultivosEnc.get(position).getEstacionSiembra());
        holder.region.setText(cultivosEnc.get(position).getRegion());
        // se procesa la imagen a traves de una libreria "picasso"
        Picasso.get()
                .load(cultivosEnc.get(position).getImagen())
                .error(R.mipmap.logo)
                .into(holder.icono);
        Log.i("tag", "url: " +cultivosEnc.get(position).getImagen());
    }

    // se cuenta el total de elementos a renderizar
    @Override
    public int getItemCount() {
        // solo cuenta cuandos modelos se han pasado al recyclerview
        return cultivosEnc.size();
    }

    //oncreate
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // es como una especie de 'oncreate'

        // atributos del modelo / tarjeta
        ImageView icono;
        TextView nombre, temperatura, estacion, region;

        //constructor
        public MyViewHolder(@NonNull View itemView, EnciclopediaInterface cultivosEnc) {
            super(itemView);
            // se identifican los atributos de las tarjetas
            icono = itemView.findViewById(R.id.ic_cultivo);
            nombre = itemView.findViewById(R.id.titulo_cult_card);
            temperatura = itemView.findViewById(R.id.titulo_temp_card);
            estacion = itemView.findViewById(R.id.titulo_est_card);
            region = itemView.findViewById(R.id.titulo_reg_card);

            // se agrega un "clicklistenerevent" a cada elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cultivosEnc != null) {
                        // si no es nulo se extrae su posicion
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            cultivosEnc.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
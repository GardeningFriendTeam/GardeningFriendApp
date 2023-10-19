package com.maid.gardeningfriend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EnciclopediaRecyclerView extends RecyclerView.Adapter<EnciclopediaRecyclerView.MyViewHolder> {
    //atributos
    private final EnciclopediaInterface enciclopediaInterface;

    Context context;
    ArrayList<CultivosGenerador> cultModel;

    // constructor
    public EnciclopediaRecyclerView(Enciclopedia enciclopedia, ArrayList<CultivosGenerador> cultModelo, EnciclopediaInterface enciclopediaInterface) {
        this.context = context;
        this.cultModel = cultModelo;
        this.enciclopediaInterface = enciclopediaInterface;
    }



    @NonNull
    @Override
    public EnciclopediaRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_cultivos, parent, false);

        return new EnciclopediaRecyclerView.MyViewHolder(view, enciclopediaInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull EnciclopediaRecyclerView.MyViewHolder holder, int position) {
        // asigna un valor numérico representando la posición de cada tarjeta
        holder.nombre.setText(cultModel.get(position).getNombre());
        holder.temperatura.setText(cultModel.get(position).getTemperatura());
        holder.estacion.setText(cultModel.get(position).getEstacionSiembra());
        holder.region.setText(cultModel.get(position).getRegion());
        // se procesa la imagen a traves de una libreria "picasso"
        Picasso.get()
                .load(cultModel.get(position).getImagen())
                .error(R.mipmap.logo)
                .into(holder.icono);
        Log.i("tag", "url: " + cultModel.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        // solo cuenta cuandos modelos se han pasado al recyclerview
        return cultModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icono;

        TextView nombre, temperatura, estacion, region;


        public MyViewHolder(@NonNull View itemView, EnciclopediaInterface enciclopediaInterface) {
            super(itemView);
            // se identifican los atributos de las tarjetas
            icono = itemView.findViewById(R.id.ic_cultivo);
            nombre = itemView.findViewById(R.id.titulo_cult_card);
            temperatura = itemView.findViewById(R.id.titulo_temp_card);
            estacion = itemView.findViewById(R.id.titulo_est_card);
            region = itemView.findViewById(R.id.titulo_reg_card);


            // agregar un onClick() a cada elemento
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (enciclopediaInterface != null) {
                        // si no es nulo se extrae su posicion
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            enciclopediaInterface.onItemClick(pos);
                        }
                    }
                }

            });
        }
    }
}

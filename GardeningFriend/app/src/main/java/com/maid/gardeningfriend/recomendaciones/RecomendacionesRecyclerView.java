package com.maid.gardeningfriend.recomendaciones;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * funcionalidades necesarias para insertar cultivos
 * en recyclerview
 */
public class RecomendacionesRecyclerView extends RecyclerView.Adapter<RecomendacionesRecyclerView.MyViewHolder> {
    // atributos
    private final RecomendacionesInterface recoInterfaz;
    Context context;
    ArrayList<CultivosGenerador> cultModel;

    // constrcutor
    public RecomendacionesRecyclerView(Context context, ArrayList<CultivosGenerador> cultModelo, RecomendacionesInterface recoInterfaz){
        this.context = context;
        this.cultModel = cultModelo;
        this.recoInterfaz = recoInterfaz;
    }

    @NonNull
    @Override
    public RecomendacionesRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "inflates" el layout con los estilos / modelos definidos
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_cultivos, parent, false);

        return new RecomendacionesRecyclerView.MyViewHolder(view,recoInterfaz);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomendacionesRecyclerView.MyViewHolder holder, int position) {
        // asigna un valor numerico representado la posicion de cada tarjeta
        holder.nombre.setText(cultModel.get(position).getNombre());
        holder.temperatura.setText(cultModel.get(position).getTemperatura());
        holder.estacion.setText(cultModel.get(position).getEstacionSiembra());
        holder.region.setText(cultModel.get(position).getRegion());
        // se procesa la imagen a traves de una libreria "picasso"
        Picasso.get()
                .load(cultModel.get(position).getImagen())
                .error(R.mipmap.logo)
                .into(holder.icono);
        Log.i("tag", "url: " +cultModel.get(position).getImagen());

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
        ImageButton fav;

        //constructor
        public MyViewHolder(@NonNull View itemView, RecomendacionesInterface recoInterfaz){
            super(itemView);
            // se identifican los atributos de las tarjetas
            icono = itemView.findViewById(R.id.ic_cultivo);
            nombre = itemView.findViewById(R.id.titulo_cult_card);
            temperatura = itemView.findViewById(R.id.titulo_temp_card);
            estacion = itemView.findViewById(R.id.titulo_est_card);
            region = itemView.findViewById(R.id.titulo_reg_card);
            fav = itemView.findViewById(R.id.btn_fav);

            // se agrega un "clicklistenerevent" a cada elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recoInterfaz != null){
                        // si no es nulo se extrae su posicion
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recoInterfaz.onItemClick(pos);
                        }
                    }
                }
            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recoInterfaz != null){
                        // si no es nulo se extrae su posicion
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recoInterfaz.onFavClick(pos);
                        }
                    }
                }
            });



        }
    }

}

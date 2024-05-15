package com.maid.gardeningfriend.favoritos;

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

import com.maid.gardeningfriend.recomendaciones.CultivosGenerador;
import com.maid.gardeningfriend.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritosRecyclerView extends RecyclerView.Adapter<FavoritosRecyclerView.MyViewHolder> {
    //Atributos
    private final FavoritosInterface favoritosInterface;
    Context context;

    ArrayList<CultivosGenerador> cultivosListaFavs;

    //constructor
    public FavoritosRecyclerView(Context context, ArrayList<CultivosGenerador> cultivosListaFavs, FavoritosInterface favoritosInterface) {
        this.context = context;
        this.cultivosListaFavs = cultivosListaFavs;
        this.favoritosInterface = favoritosInterface;
        Log.i("adtConst", cultivosListaFavs.toString());
    }

    //metodo que crea infla las tarjetas en el recyclerview
    @NonNull
    @Override
    public FavoritosRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_favoritos,parent,false);

        return new FavoritosRecyclerView.MyViewHolder(view,favoritosInterface);

    }

    //metodo que realiza un binding entre la informacion del cultivo y la info del array
    @Override
    public void onBindViewHolder(@NonNull FavoritosRecyclerView.MyViewHolder holder, int position) {
        holder.nombre.setText(cultivosListaFavs.get(position).getNombre());
        holder.temperatura.setText(cultivosListaFavs.get(position).getTemperatura());
        holder.estacion.setText(cultivosListaFavs.get(position).getEstacionSiembra());
        holder.region.setText(cultivosListaFavs.get(position).getRegion());
        // se usa la libreria Picasso para procesar la imagen
        Picasso.get()
                .load(cultivosListaFavs.get(position).getImagen())
                .error(R.mipmap.logo)
                .into(holder.icono);
    }

    // obtiene la cantidad de elementos del array base
    @Override
    public int getItemCount() {
        return cultivosListaFavs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //atributos de la tarjeta:
        TextView nombre, temperatura, region, estacion;
        ImageButton btnInfo, btnEliminar;
        ImageView icono;

        public MyViewHolder(@NonNull View view, FavoritosInterface favoritosInterface){
            super(view);
            nombre = view.findViewById(R.id.nombre_cultivo_favs);
            temperatura = view.findViewById(R.id.temp_cultivo_favs);
            region = view.findViewById(R.id.est_cultivo_favs);
            estacion = view.findViewById(R.id.reg_cultivo_favs);
            btnInfo = view.findViewById(R.id.btn_info_favs);
            btnEliminar = view.findViewById(R.id.btn_eliminar_favs);
            icono = view.findViewById(R.id.ic_favs);

            //se agregan las funcionalidades para identificar la tarjeta cuando se clickea
            // la opcion ver 'info' o 'elimiar'
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favoritosInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            favoritosInterface.OninfoFav(pos);
                        }

                    }

                }
            });

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favoritosInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            favoritosInterface.OnEliminarFav(pos);
                        }

                    }

                }
            });
        }
    }


}

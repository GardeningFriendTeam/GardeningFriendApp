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
import com.maid.gardeningfriend.recomendaciones.CultivosGenerador;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PanelAdminCultivosRecyclerView extends RecyclerView.Adapter<PanelAdminCultivosRecyclerView.MyViewHolder> {
    //atributos
    private final PanelAdminInterfaceCultivos panelAdminInterface;
    Context context;
    ArrayList<CultivosGenerador> cultivos;

    //constructor
    public PanelAdminCultivosRecyclerView(Context context, ArrayList<CultivosGenerador> cultivos, PanelAdminInterfaceCultivos panelAdminInterface) {
        this.context = context;
        this.cultivos = cultivos;
        this.panelAdminInterface = panelAdminInterface;
    }

    @NonNull
    @Override
    public PanelAdminCultivosRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout con las tarjetas creadas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_cultivos_panel_admin,parent,false);

        return new PanelAdminCultivosRecyclerView.MyViewHolder(view,panelAdminInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelAdminCultivosRecyclerView.MyViewHolder holder, int position) {
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
        Button btnEliminar, btnEditar;

        //constructor
        public MyViewHolder(@NonNull View itemView, PanelAdminInterfaceCultivos panelAdminInterface){
            super(itemView);
            icono = itemView.findViewById(R.id.ic_card_cultivos);
            titulo = itemView.findViewById(R.id.nombre_cultivo);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);

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

}

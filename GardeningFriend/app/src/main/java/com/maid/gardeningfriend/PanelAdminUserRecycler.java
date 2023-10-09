package com.maid.gardeningfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PanelAdminUserRecycler extends RecyclerView.Adapter<PanelAdminUserRecycler.MyViewHolder>{
    //Atributos
    Context context;
    ArrayList<UsuariosGenerador> usuarios;

    //constructor
    public PanelAdminUserRecycler(Context context, ArrayList<UsuariosGenerador> usuarios) {
        this.context = context;
        this.usuarios = usuarios;
    }

    //metodo para inflar la tarjeta e insertarla en el recyclerview
    @NonNull
    @Override
    public PanelAdminUserRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout con las tarjetas creadas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_usuarios_panel_admin, parent, false);

        return new PanelAdminUserRecycler.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelAdminUserRecycler.MyViewHolder holder, int position) {
        // asigna un index a cada elem
        // y extrae las propiedades de cada objeto del cultivo
        holder.nombreUser.setText(usuarios.get(position).getName());
        String rolUser;
        // se determina si es admin o no
        if (usuarios.get(position).isAdmin()) {
            rolUser = "Admin";
        } else {
            rolUser = "Usuario";
        }
        holder.rolUser.setText(rolUser);
    }

    @Override
    public int getItemCount() {
        // identifica cuantos cultivos deben mostrarse
        return usuarios.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // basicamente crea las tarjetas de los cultivos
        // atributos
        TextView nombreUser, rolUser;
        Button btnEliminarUser, btnEditarRol;

        //constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreUser = itemView.findViewById(R.id.nombre_user);
            rolUser = itemView.findViewById(R.id.nombre_user);
            btnEditarRol = itemView.findViewById(R.id.btn_cambiar_rol);
            btnEliminarUser = itemView.findViewById(R.id.btn_eliminar_user);

        }
    }

}



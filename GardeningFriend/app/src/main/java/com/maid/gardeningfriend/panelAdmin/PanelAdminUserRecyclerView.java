package com.maid.gardeningfriend.panelAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.maid.gardeningfriend.R;

import java.util.ArrayList;

public class PanelAdminUserRecyclerView extends RecyclerView.Adapter<PanelAdminUserRecyclerView.MyViewHolder>{
    //Atributos
    Context context;
    ArrayList<UsuariosGenerador> usuarios;
    private final PanelAdminInterfaceUsuarios panelAdminInterface;

    //constructor
    public PanelAdminUserRecyclerView(Context context, ArrayList<UsuariosGenerador> usuarios, PanelAdminInterfaceUsuarios panelAdminInterface) {
        this.context = context;
        this.usuarios = usuarios;
        this.panelAdminInterface = panelAdminInterface;
    }

    //metodo para inflar la tarjeta e insertarla en el recyclerview
    @NonNull
    @Override
    public PanelAdminUserRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla el layout con las tarjetas creadas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards_usuarios_panel_admin, parent, false);

        return new PanelAdminUserRecyclerView.MyViewHolder(view,panelAdminInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelAdminUserRecyclerView.MyViewHolder holder, int position) {
        // asigna un index a cada elem
        // y extrae las propiedades de cada objeto del cultivo
        holder.nombreUser.setText(usuarios.get(position).getName());
        // se determina si es admin o no
        if (usuarios.get(position).isAdmin()) {
            holder.rolUser.setText("Admin");
        } else {
            holder.rolUser.setText("Usuario");
        }
    }

    @Override
    public int getItemCount() {
        // identifica cuantos cultivos deben mostrarse
        return usuarios.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // basicamente crea las tarjetas de los cultivos
        // atributos
        TextView nombreUser;
        TextView rolUser;
        AppCompatButton btnEliminarUser;
        AppCompatButton btnEditarRol;

        //constructor
        public MyViewHolder(@NonNull View itemView, PanelAdminInterfaceUsuarios panelAdminInterface) {
            super(itemView);
            nombreUser = itemView.findViewById(R.id.nombre_user);
            rolUser = itemView.findViewById(R.id.rol_user);
            btnEditarRol = itemView.findViewById(R.id.btn_cambiar_rol);
            btnEliminarUser = itemView.findViewById(R.id.btn_eliminar_user);


            //permite identificar sobre que tarjeta se clickeo el botonC
            btnEliminarUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(panelAdminInterface != null){
                        // si no es nulo se obtiene la posicion (index) de la tarjeta
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            panelAdminInterface.eliminarUserBtn(pos);
                        }
                    }
                }
            });

            //permite identificar sobre que tarjeta se clickeo el boton
            btnEditarRol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(panelAdminInterface != null){
                        // si no es nulo se obtiene la posicion (index) de la tarjeta
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            panelAdminInterface.cambiarRolUserBtn(pos);
                        }
                    }
                }
            });

        }
    }



}



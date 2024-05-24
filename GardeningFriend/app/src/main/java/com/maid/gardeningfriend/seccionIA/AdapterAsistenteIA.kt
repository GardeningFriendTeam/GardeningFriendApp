package com.maid.gardeningfriend.seccionIA

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maid.gardeningfriend.R

class AdapterAsistenteIA
    (var context: Context, var respuestas: ArrayList<ModelRespuestaIA>, //atributos
     private val interfaceAsistenteIA: InterfaceAsistenteIA
) :
    RecyclerView.Adapter<AdapterAsistenteIA.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // infla el layout con las tarjetas creadas
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.cards_respuesta_ia, parent, false)
        return MyViewHolder(view, interfaceAsistenteIA)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // base text
        val text = respuestas[position].texto

        // splitting text
        val words = text.split(" ")

        // Take the first 5 words from the list of words
        val firstSixWords = words.take(6)

        // assigning value to title
        holder.titulo.text = firstSixWords.toString()

    }

    override fun getItemCount(): Int {
        // identifica cuantos cultivos deben mostrarse
        return respuestas.size
    }

    class MyViewHolder(itemView: View, interfaceAsistenteIA: InterfaceAsistenteIA) :
        RecyclerView.ViewHolder(itemView) {
        // basicamente crea las tarjetas de los cultivos
        // atributos
        var titulo: TextView
        var btnEliminar: Button
        var btnAbrir: Button

        //constructor
        init {
            // initializing properties
            titulo = itemView.findViewById(R.id.titulo_card_fav_ia)
            btnAbrir = itemView.findViewById(R.id.open_card_fav_ia)
            btnEliminar = itemView.findViewById(R.id.delete_card_fav_ia)

            //permite identificar sobre que tarjeta se clickeo el botonC
            btnEliminar.setOnClickListener(View.OnClickListener {
                // si no es nulo se obtiene la posicion (index) de la tarjeta
                val pos = getBindingAdapterPosition()
                if (pos != RecyclerView.NO_POSITION) {
                    interfaceAsistenteIA.eliminarBtn(pos)
                }
            })

            //permite identificar sobre que tarjeta se clickeo el boton
            btnAbrir.setOnClickListener(View.OnClickListener {
                // si no es nulo se obtiene la posicion (index) de la tarjeta
                val pos = getBindingAdapterPosition()
                if (pos != RecyclerView.NO_POSITION) {
                    interfaceAsistenteIA.abrirBtn(pos)
                }
            })
        }
    }
}

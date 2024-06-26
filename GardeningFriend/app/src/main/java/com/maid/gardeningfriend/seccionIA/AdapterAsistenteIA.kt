package com.maid.gardeningfriend.seccionIA

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.maid.gardeningfriend.R
import com.squareup.picasso.Picasso

class AdapterAsistenteIA
    (var context: Context, var respuestas: ArrayList<ModelRespuestaIA>,
     private val interfaceAsistenteIA: InterfaceAsistenteIA
) :
    RecyclerView.Adapter<AdapterAsistenteIA.MyViewHolder>() {

    // Lista para almacenar todos los datos originales sin filtrar
    private var respuestasOriginales: ArrayList<ModelRespuestaIA> = ArrayList(respuestas)

    // Variable para indicar si la lista está vacía
    private var isEmptyList: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // infla el layout con las tarjetas creadas
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.cards_respuesta_ia, parent, false)
        return MyViewHolder(view, interfaceAsistenteIA)
    }

    // Método para filtrar las respuestas favoritas por texto
    fun filter(text: String) {
        respuestas.clear()
        if (text.isEmpty()) {
            respuestas.addAll(respuestasOriginales)
        } else {
            val query = text.toLowerCase()
            respuestasOriginales.forEach {
                if (it.texto.toLowerCase().contains(query)) {
                    respuestas.add(it)
                }
            }
        }
        // Actualizar la variable isEmptyList
        isEmptyList = respuestas.isEmpty()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (isEmptyList) {
            // Mostrar un mensaje indicando que no se encontraron respuestas
            holder.titulo.text = context.getString(R.string.no_results_found)
            // Ocultar la imagen
            holder.imagePlant.visibility = View.GONE
        } else {
            // El código para mostrar las respuestas normales
            // ------------------- TITLE -------------------------//
            // base text
            val text = respuestas[position].texto
            // splitting text
            val words = text.split(" ")
            // Take the first 5 words from the list of words
            val firstSixWords = words.take(6)
            // joins the strings back
            val finalText = firstSixWords.joinToString(" ")
            // assigning value to title
            holder.titulo.text = finalText
            // ------------------- IMAGE -------------------------//
            Picasso.get()
                .load(respuestas.get(position).imagenLink)
                .error(R.mipmap.logo)
                .into(holder.imagePlant)
            Log.i("tag", "url: " + respuestas.get(position).imagenLink)
        }
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
        var imagePlant : ShapeableImageView

        //constructor
        init {
            // initializing properties
            titulo = itemView.findViewById(R.id.titulo_card_fav_ia)
            btnAbrir = itemView.findViewById(R.id.open_card_fav_ia)
            btnEliminar = itemView.findViewById(R.id.delete_card_fav_ia)
            imagePlant = itemView.findViewById(R.id.ic_card_fav_ia)

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

    fun removeItem(position: Int) {
        if (respuestas.size > 1){
            respuestas.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun filterList(filteredList: ArrayList<ModelRespuestaIA>) {
        respuestas.clear()
        respuestas.addAll(filteredList)
        notifyDataSetChanged()
    }
}

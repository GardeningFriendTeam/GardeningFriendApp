package com.maid.gardeningfriend.seccionIA

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maid.gardeningfriend.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDisplayFavs : AppCompatActivity(), InterfaceAsistenteIA {

    private var favs = ArrayList<ModelRespuestaIA>()
    private var recyclerView: RecyclerView? = null
    private var adapter: AdapterAsistenteIA? = null
    private var favsOriginal = ArrayList<ModelRespuestaIA>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_favs)

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.ai_favs_recyclerview)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Obtener las respuestas favoritas
        getFavResponsesAI()

        // Configurar el SearchView
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filter(it) }
                return true
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getFavResponsesAI() {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("respuestasIA")

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = user?.email.toString()

        val toastError = Toast.makeText(
            applicationContext,
            "error al conectar con la BD",
            Toast.LENGTH_SHORT
        )

        lifecycleScope.launch(Dispatchers.IO) {
            collection
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val data = document.toObject(ModelRespuestaIA::class.java)
                        favs.add(data)
                    }
                    // Guardar una copia de las respuestas originales
                    favsOriginal.addAll(favs)

                    // Crear y configurar el adaptador
                    adapter = AdapterAsistenteIA(applicationContext, favs, this@ActivityDisplayFavs)
                    recyclerView!!.adapter = adapter
                }.addOnFailureListener { exception ->
                    toastError.show()
                    Log.e("RESPUESTAS_IA", "ERROR GET REQUEST", exception)
                }
        }
    }

    override fun eliminarBtn(position: Int) {
        val toastError = Toast.makeText(
            applicationContext,
            "no se ha podido completar la operacion",
            Toast.LENGTH_SHORT
        )

        val toastSuccess = Toast.makeText(
            applicationContext,
            "operacion completada!",
            Toast.LENGTH_SHORT
        )

        val selectedCard = favs[position]

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("respuestasIA")
        val document = collection.document(selectedCard.id)

        document
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastSuccess.show()
                } else {
                    toastError.show()
                    Log.e("DELETE_IA_CARD", "ERROR REQUEST", task.exception)
                }
            }

        favs.remove(selectedCard)
        adapter?.removeItem(position)
    }

    override fun abrirBtn(position: Int) {
        val selectedCard = favs[position]
        val intent = Intent(this, ActivityShowResponse::class.java)
        intent.putExtra("CARD_IA_SELECTED", selectedCard)
        startActivity(intent)
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<ModelRespuestaIA>()
        for (item in favsOriginal) {
            if (item.texto.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adapter?.filterList(filteredList)
    }
}

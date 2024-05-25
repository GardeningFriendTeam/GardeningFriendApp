package com.maid.gardeningfriend.seccionIA

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maid.gardeningfriend.MainActivity
import com.maid.gardeningfriend.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDisplayFavs : MainActivity(), InterfaceAsistenteIA{

    // state which contains all the fetched documents
    var favs = ArrayList<ModelRespuestaIA>()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_favs)
        // calling fetch request
        getFavResponsesAI()
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getFavResponsesAI(){
        // instantiating firebase
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("respuestasIA")

        // instantiating auth service
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = if (user != null) user.email.toString() else ""

        // error toast message
        val toastError = Toast.makeText(
            applicationContext,
            "error al conectar con la BD",
            Toast.LENGTH_SHORT
        )

        // using global scope to avoid concurrency problems
        lifecycleScope.launch(Dispatchers.IO){
            collection
                // filtering documents
                .whereEqualTo("userEmail", userEmail)
                // executing request
                .get()
                // handling results
                .addOnSuccessListener { result ->
                    for (document in result){
                        // deserializing document
                        val data = document.toObject(ModelRespuestaIA::class.java)
                        // adding document to arraylist
                        favs.add(data)
                    }
                    // once the request is done the recyclerView is activated
                    activarAdapterSeccionAI()
                }.addOnFailureListener { exception ->
                    toastError.show()
                    Log.e("RESPUESTAS_IA", "ERROR GET REQUEST", exception)
                }
        }
    }

    fun activarAdapterSeccionAI() {
        //se identifica el recycler view
        val recyclerAdmin = findViewById<RecyclerView>(R.id.ai_favs_recyclerview)
        // se crea el adapter para recyclerview
        val adapter = AdapterAsistenteIA(applicationContext,favs, this)
        recyclerAdmin.adapter = adapter
        recyclerAdmin.layoutManager = LinearLayoutManager(this)
    }

    /**
     * deletes the card from the state and also the DB
     */
    override fun eliminarBtn(position: Int) {
        // defining toast messages
        val toastError = Toast.makeText(
            applicationContext,
            "no se ha podido completar la operacion",
            Toast.LENGTH_SHORT)

        val toastSuccess = Toast.makeText(
            applicationContext,
            "operacion completada!",
            Toast.LENGTH_SHORT
        )

        // identifying selected card
        val selectedCard = favs.get(position)

        // instantiating firebase and identifying document
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("respuestasIA")
        val document = collection.document(selectedCard.id)

        // removing from firebase
        document
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    toastSuccess.show()
                } else {
                    toastError.show()
                    Log.e("DELETE_IA_CARD", "ERROR REQUEST", task.exception)
                }
            }

        // remocing locally
        favs.remove(selectedCard)
    }

    override fun abrirBtn(position: Int) {
        // identifying selected card
        val selectedCard = favs.get(position)
        // defining intent to open new activity
        val intent = Intent(this,ActivityShowResponse::class.java)
        intent.putExtra("CARD_IA_SELECTED", selectedCard)
        // starting new activity
        startActivity(intent)
    }

}
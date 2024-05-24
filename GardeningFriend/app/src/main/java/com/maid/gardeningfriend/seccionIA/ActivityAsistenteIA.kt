package com.maid.gardeningfriend.seccionIA

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maid.gardeningfriend.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Suppress("DEPRECATION")
class ActivityAsistenteIA : AppCompatActivity() {
    // properties
    private val PICK_IMAGE = 1
    private var imageView: ImageView? = null
    private var buttonUpload: Button? = null
    private var buttonDelete: Button? = null
    private var textViewResponseIA: TextView? = null
    private var imageUpload : Bitmap? = null
    private var geminiProVision: GenerativeModel? = null
    private var messagePrompt: String? = null
    private var geminiResponse: String? = null
    private var buttonAddToFavs: Button? = null
    private var buttonDisplayFavs : Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistente_ia)

        // identificando propiedades
        imageView = findViewById(R.id.ic_picture)
        buttonUpload = findViewById(R.id.btn_subir_img)
        textViewResponseIA = findViewById(R.id.ia_response)
        buttonDelete = findViewById(R.id.btn_eliminar_img)
        buttonAddToFavs = findViewById(R.id.btn_favs_ai)
        buttonDisplayFavs = findViewById(R.id.btn_open_favs_section_ia)

        // adding function to btn upload
        buttonUpload!!.setOnClickListener{ v: View? -> openGallery() }

        // adding function to delete image
        buttonDelete!!.setOnClickListener { v: View? -> deletePhoto() }

        // adding function to add to fav btn
        buttonAddToFavs!!.setOnClickListener { v: View? -> addNewFav()}

        // adding function to display fav responses
        buttonDisplayFavs!!.setOnClickListener {
            Log.i("OPEN_AI_FAVS", "ACTIVITY OPEN")
            val intent = Intent(this, ActivityDisplayFavs::class.java)
            startActivity(intent)
        }

        // defining LLM
        //TODO: hide apykey for security reasons!
        geminiProVision = GenerativeModel("gemini-pro-vision", "AIzaSyACRZhR_TnmticRhpOolXD00TVILiXhh_8")

        // defining prompt
        messagePrompt = "puedes identificar que planta o cultivo es? ademas, dime sus principales caracteristicas"
    }

    /**
     * esta funcion inicia un intent para abrir una nueva pestana donde se podra seleccionar la img
     */
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE)
    }

    /**
     * esta funcion selecciona propiamente la img y la almacena en la propiedad especificada
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode The integer result code returned by the child activity
     * through its setResult().
     * @param data An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            try {
                // selecting image
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                imageView!!.setImageBitmap(bitmap);
                imageUpload = bitmap;
                // sending request to LLM and storing its response
                getGeminiResponse()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * removes image and ia response
     */
    protected fun deletePhoto() {
        imageView!!.setImageResource(0)
        textViewResponseIA!!.clearComposingText()
        imageUpload = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getGeminiResponse() {

        // if image is null the function ends
        if (imageUpload == null) return

        // sending request to LLM
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val feedback = geminiProVision!!.generateContent(
                    content {
                        text(messagePrompt!!)
                        image(imageUpload!!)
                    }
                )

                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    textViewResponseIA?.text = feedback.text.toString()
                    geminiResponse = feedback.text.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * adds the AI response to a firebase collection "respuestasIA"
     */
    fun addNewFav(){
        // toast messages
        val toastOK = Toast.makeText(
            applicationContext,
            "tu respuesta se ha agregado a favs!",
            Toast.LENGTH_SHORT)

        val toastError = Toast.makeText(
            applicationContext,
            "no se ha podido agregar a fav!",
            Toast.LENGTH_SHORT
        )

        val toastLoginFirst = Toast.makeText(
            applicationContext,
            "debes loguearte para acceder a esta funcionalidad!",
            Toast.LENGTH_SHORT
        )

        // instanciating firebase
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("respuestasIA")

        // extracting user's info
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = if (user != null){
            user.email.toString()
        } else {
            toastLoginFirst.show()
            return
        }

        // creating document object
        val fav = ModelRespuestaIA(
            userEmail = userEmail.toString(),
            texto = geminiResponse.toString()
        )

        // executing add request
        collection
            .add(fav)
            .addOnCompleteListener { task ->
                // handling results
                if (task.isSuccessful){
                    toastOK.show()
                } else {
                    toastError.show()
                    Log.e("AI_FAVS", "ERROR ADDING DOC", task.exception)
                }
            }
    }

}
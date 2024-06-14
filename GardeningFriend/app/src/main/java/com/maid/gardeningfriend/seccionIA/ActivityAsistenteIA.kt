package com.maid.gardeningfriend.seccionIA

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.maid.gardeningfriend.MainActivity
import com.maid.gardeningfriend.R
import com.maid.gardeningfriend.login.Login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

@Suppress("DEPRECATION")
class ActivityAsistenteIA : MainActivity() {
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
    private var auth: FirebaseAuth? = null;
    private var currentUser: FirebaseUser? = null;
    private var progressBarFav: ProgressBar? = null
    private var textViewProgressBarFav: TextView? = null
    private var progressBarResponse : ProgressBar? = null
    private var textViewProgressBarResponse : TextView? = null
    private var geminiApiKey: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_asistente_ia, findViewById(R.id.content_frame))

        // instatiating firebase auth
        auth = FirebaseAuth.getInstance()
        currentUser = auth?.currentUser

        // if user is not loged in, it's redirected lo login page
        if (currentUser == null){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // identificando propiedades
        imageView = findViewById(R.id.ic_picture)
        buttonUpload = findViewById(R.id.btn_subir_img)
        textViewResponseIA = findViewById(R.id.ia_response)
        buttonDelete = findViewById(R.id.btn_eliminar_img)
        buttonAddToFavs = findViewById(R.id.btn_favs_ai)
        buttonDisplayFavs = findViewById(R.id.btn_open_favs_section_ia)
        progressBarFav = findViewById(R.id.progressbar_ia)
        textViewProgressBarFav = findViewById(R.id.progressbar_ia_textView)
        progressBarResponse = findViewById(R.id.progressbar_ia_response)
        textViewProgressBarResponse = findViewById(R.id.progressbar_ia_response_textView)

        // extracting apiKeys
        geminiApiKey = resources.getString(R.string.gemini_api_key);

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
        geminiProVision = GenerativeModel("gemini-pro-vision", geminiApiKey!!)

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


    fun getGeminiResponse() {
        // displaying loader
        progressBarResponse!!.visibility = View.VISIBLE
        textViewProgressBarResponse!!.visibility = View.VISIBLE

        // if image is null the function ends
        if (imageUpload == null) return

        // sending request to LLM
        lifecycleScope.launch(Dispatchers.IO) {
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
            // hidding loader once the operation was performed
            progressBarResponse!!.visibility = View.GONE
            textViewProgressBarResponse!!.visibility = View.GONE
        }
    }

    /**
     * adds the AI response to a firebase collection "respuestasIA"
     */
    fun addNewFav(){
        // displaying loader
        progressBarFav!!.visibility = View.VISIBLE
        textViewProgressBarFav!!.visibility = View.VISIBLE

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
        val userID = user.uid

        // uploading image to firebase storage
        // instantiating storage service and necessary resources
        val firebaseStorage = FirebaseStorage.getInstance()
        val filename = UUID.randomUUID().toString() + ".jpg"
        val imageRef = firebaseStorage.reference.child("usuarios/$userID/$filename")
        val imageUri = saveBitmapToFile(this,imageUpload!!,filename)
        // executing async function to get download link
        lifecycleScope.launch {
            val imgLink = uploadImageToStorage(imageRef, imageUri!!)
            // Use imgLink as needed
            if (imgLink.isNotEmpty()) {
                // creating document object
                val fav = ModelRespuestaIA(
                    userEmail = userEmail,
                    texto = geminiResponse.toString(),
                    imagenLink = imgLink
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
            } else {
                Log.e("UPLOAD_FAILURE", "Failed to upload image.")
            }
            // hiding loader once the operation was performed
            progressBarFav!!.visibility = View.GONE
            textViewProgressBarFav!!.visibility = View.GONE
        }

    }

    /**
     * converts the bitmap file into a uri one
     */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        // Get the directory for the app's private pictures directory
        val directory = context.getExternalFilesDir("images")
        if (directory == null) {
            return null
        }

        // Create a file in the specified directory
        val file = File(directory, fileName)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            // Use the compress method on the Bitmap object to write the image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            // Flush and close the OutputStream
            fileOutputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        // Return the URI of the file
        return Uri.fromFile(file)
    }

    /**
     *  function that uploads image to storage and returns its download link
     */
    suspend fun uploadImageToStorage(path: StorageReference, imageUri: Uri): String {
        return try {
            val uploadTask = path.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("ERROR_UPLOADING_IMG_STORAGE", "OPERATION_FAILED", e)
            ""
        }
    }



}
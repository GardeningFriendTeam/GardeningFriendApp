package com.maid.gardeningfriend.seccionIA

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.maid.gardeningfriend.MainActivity
import com.maid.gardeningfriend.R
import com.squareup.picasso.Picasso

class ActivityShowResponse : MainActivity() {
    // setting properties
    var text: TextView? = null;
    var model: ModelRespuestaIA? = null;
    var image: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_response)

        // getting serializable from intent
        model = intent.extras?.getSerializable("CARD_IA_SELECTED") as? ModelRespuestaIA

        // displaying content in textView
        text = findViewById(R.id.text_show_ia_response)
        image = findViewById(R.id.ic_plant_ia)
        text!!.text = model!!.texto

        // using picasso to render image
        Picasso.get()
            .load(model!!.imagenLink)
            .error(R.mipmap.logo)
            .into(image)
    }
}
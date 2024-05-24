package com.maid.gardeningfriend.seccionIA

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.maid.gardeningfriend.R

class ActivityShowResponse : AppCompatActivity() {
    // setting properties
    var text: TextView? = null;
    var model: ModelRespuestaIA? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_response)

        // getting serializable from intent
        model = intent.extras?.getSerializable("CARD_IA_SELECTED") as? ModelRespuestaIA

        // displaying content in textView
        text = findViewById(R.id.text_show_ia_response)
        text!!.text = model!!.texto
    }
}
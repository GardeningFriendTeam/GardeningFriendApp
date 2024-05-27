package com.maid.gardeningfriend.seccionIA

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class ModelRespuestaIA(
    var userEmail: String = "",
    var texto:String = "",
    var imagenLink:String = "",
    @DocumentId
    var id:String = ""
):Serializable

package com.maid.gardeningfriend.notas;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maid.gardeningfriend.recursosVarios.AlarmReceiver;

import java.text.SimpleDateFormat;

public class Utility {

    // Este método muestra un mensaje de notificación Toast en la pantalla.
    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Este método obtiene una referencia a una colección de documentos en Firestore.
    // Se utiliza para interactuar con una colección llamada "mis_notas" específica del usuario actual.
    // Ayuda a organizar y recuperar datos relacionados con las notas de un usuario.
    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notas")
                .document(currentUser.getUid()).collection("mis_notas");
    }

    // Convierte un objeto de tipo Timestamp de Firebase en una representación de cadena de fecha formateada.
    // Esto es útil para mostrar fechas almacenadas en Firestore en un formato más legible para el usuario.
    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }

    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        if (timestamp > System.currentTimeMillis()) {
            // Schedule the alarm
            alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
        } else {
            // The specified time is in the past; handle this case accordingly
            Toast.makeText(ctx, "Esta configurando la alarma para el mismo horario", Toast.LENGTH_SHORT).show();
        }

    }
}

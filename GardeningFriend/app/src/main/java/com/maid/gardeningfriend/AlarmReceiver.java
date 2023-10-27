package com.maid.gardeningfriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("note_title");
        String content = intent.getStringExtra("note_content");

        // Lógica para mostrar la notificación aquí, usando los datos recibidos
        // (la lógica que tenías en tu NotificationWorker)
        showNotification(context, title, content);
    }

    private void showNotification(Context context, String title, String content) {
        // (Lógica para mostrar la notificación, similar a tu NotificationWorker)
    }
}


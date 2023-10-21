package com.maid.gardeningfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView, deleteTextViewBtn, notificationsTime;
    Button changeAlarmBtn;
    LinearLayout linearLayoutTime;
    private final int alarmID = 1;
    private String docId;
    String title, content;
    boolean isEditMode = false;

    public static String CHANNEL_ID = "gardening_friend";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        // Obtener referencias a elementos de la interfaz de usuario
        titleEditText = findViewById(R.id.note_title_text);
        contentEditText = findViewById(R.id.note_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteTextViewBtn = findViewById(R.id.delete_note_text_view);
        notificationsTime = findViewById(R.id.notifications_time);
        changeAlarmBtn = findViewById(R.id.change_notification);
        linearLayoutTime = findViewById(R.id.linearLayoutTime);

        // Recibir los datos enviados desde la actividad anterior
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");


        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        // Configurar los campos de título y contenido con los datos recibidos
        titleEditText.setText(title);
        contentEditText.setText(content);

        // Si estamos en modo de edición, actualizar el título y mostrar el botón de eliminación
        if (isEditMode){
            pageTitleTextView.setText(R.string.edit_note_mode);
            deleteTextViewBtn.setVisibility(View.VISIBLE);
            linearLayoutTime.setVisibility(View.VISIBLE);
        }

        // Configurar un clic en el botón de guardar para guardar la nota
        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        // Configurar un clic en el botón de eliminación para eliminar la nota de Firebase
        deleteTextViewBtn.setOnClickListener((v -> deleteNoteFromFirebase()));

        // Configurar un click para establecere una alarma
        changeAlarmBtn.setOnClickListener((v)-> setAlarm());

    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NoteDetailsActivity.this, (timePicker, selectedHour, selectedMinute) -> {

            String finalHour, finalMinute;

            finalHour = "" + selectedHour;
            finalMinute = "" + selectedMinute;
            if (selectedHour < 10) finalHour = "0" + selectedHour;
            if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
            notificationsTime.setText(finalHour + ":" + finalMinute);

            // Crear una instancia de Data y agregar los datos necesarios
            Data inputData = new Data.Builder()
                    .putString("title", title)
                    .putString("content", content)
                    .putString("docId", docId)
                    .build();

            // Configura la alarma y el trabajo del trabajador
            setAlarmAndWork(inputData, selectedHour, selectedMinute);
        }, hour, minute, true);//Yes 24-hour time
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    private void setAlarmAndWork(Data inputData, int selectedHour, int selectedMinute) {
        Calendar calendar = Calendar.getInstance();

        // Configura la hora y la fecha de la alarma
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.SECOND, 0);

        long alarmTime = calendar.getTimeInMillis();

        // Programa la alarma
        Utility.setAlarm(alarmID, alarmTime, NoteDetailsActivity.this);

        // Programa la tarea del trabajador con los datos
        OneTimeWorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(inputData)
                .setInitialDelay(alarmTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance().enqueue(notificationWorkRequest);

        // Muestra un mensaje de confirmación al usuario
        Toast.makeText(NoteDetailsActivity.this, getString(R.string.changed_to, calendar.getTime().toString()), Toast.LENGTH_LONG).show();
    }



    // Método para guardar una nueva nota o actualizar una existente
    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle.isEmpty()){
            titleEditText.setError("Ingrese un titulo");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        // Guardar la nota en Firebase
        saveNoteToFirebase(note);
    }

    // Método para guardar la nota en Firebase Firestore
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode) {
            // Modificar la nota existente
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            // Crear una nueva nota
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // Nota guardada exitosamente
                Utility.showToast(NoteDetailsActivity.this, "Nota Agregada exitosamente");
                finish(); // Cerrar la actividad actual
            }else {
                Utility.showToast(NoteDetailsActivity.this, "Fallo al agregar la nota");
            }
        });
    }

    // Método para eliminar la nota de Firebase Firestore
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // Nota eliminada exitosamente
                Utility.showToast(NoteDetailsActivity.this, "Nota eliminada exitosamente");
                finish(); // Cerrar la actividad actual
            }else {
                Utility.showToast(NoteDetailsActivity.this, "Fallo al eliminar la nota");
            }
        });
    }
}